package peoples.materialfitness.Model.Cache;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static android.content.ContentValues.TAG;

/**
 * Created by Alex Sullivan on 10/20/2016.
 *
 * This class is used to cache personal records from the database. Prs aren't saved to the database,
 * so to figure out whats a PR we need to do a pretty big recalculation (not to mention a real
 * big hit to the datbase...) so caching those results makes sense.
 *
 */

public class DatabasePrCache implements PrCache
{
    private static DatabasePrCache INSTANCE;
    // A mapping of exercise to PR set. Note that the IDs on these set is uninitialized.
    private Map<Exercise, BehaviorSubject<WeightSet>> prMap = new HashMap<>();

    private DatabasePrCache()
    {
        buildPrMap();
    }

    public static DatabasePrCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new DatabasePrCache();
        }

        return INSTANCE;
    }

    private void buildPrMap()
    {
        new ExerciseDatabaseInteractor()
                .fetchAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(exercise -> new ExerciseSessionDatabaseInteractor().fetchWithExerciseId(exercise.getId()))
                .flatMap(exerciseSession -> new WeightSetDatabaseInteractor().fetchWithParentId(exerciseSession.getId())
                        .groupBy(weightSet -> exerciseSession.getExercise()))
                .subscribe(exerciseWeightSetGroupedObservable -> {
                    exerciseWeightSetGroupedObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .toList()
                            .subscribe(weightSets -> {
                                WeightSet set = calculatePr(weightSets);
                                pushToSubscriber(set, exerciseWeightSetGroupedObservable.getKey());
                            });
                });

    }

    // Simple helper method to get the max weight set from the provided list
    private WeightSet calculatePr(List<WeightSet> associatedSets)
    {
        WeightSet pr = new WeightSet(0, 0);

        for (WeightSet set : associatedSets)
        {
            if (set.getWeight() > pr.getWeight())
            {
                pr = set;
            }
        }

        return pr;
    }

    private void pushToSubscriber(WeightSet set, Exercise exercise)
    {
        if (prMap.containsKey(exercise))
        {
            prMap.get(exercise).onNext(set);
        }
        else
        {
            initializeExercise(set, exercise);
        }
    }

    private void initializeExercise(WeightSet set, Exercise exercise)
    {
        prMap.put(exercise, BehaviorSubject.create(set));
    }

    private void initializeExercise(Exercise exercise)
    {
        prMap.put(exercise, BehaviorSubject.create());
    }

    private void resetPrEntry(Exercise exercise)
    {
        new ExerciseSessionDatabaseInteractor()
                .fetchWithExerciseId(exercise.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(ExerciseSession::getSets)
                .flatMap(Observable::from)
                .toList()
                .subscribe(weightSets -> {
                    WeightSet set = calculatePr(weightSets);
                    pushToSubscriber(set, exercise);
                });
    }

    private BehaviorSubject<WeightSet> getPrSubjectForExercise(Exercise exercise)
    {
        if (!prMap.containsKey(exercise))
        {
            initializeExercise(exercise);
        }

        return prMap.get(exercise);
    }

    @Override
    public Observable<WeightSet> getPrForExercise(Exercise exercise)
    {
        return getPrSubjectForExercise(exercise).asObservable();
    }

    @Override
    public void weightSetModified(WeightSet editedSet, Exercise exercise)
    {
        getPrSubjectForExercise(exercise)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(weightSet -> {
                    if (weightSet.getId().equals(editedSet.getId()))
                    {
                        resetPrEntry(exercise);
                    }
                });
    }

    @Override
    public void weightSetAdded(WeightSet weightSet, Exercise exercise)
    {
        final BehaviorSubject<WeightSet> subject = getPrSubjectForExercise(exercise);
        // If this is our first weight set (at least that we've seen) its de-facto the PR.
        // Push it along.
        if (!subject.hasValue())
        {
            pushToSubscriber(weightSet, exercise);
        }
        else
        {
            // Make sure to only take the 1st item emitted by the subject. The reason for this is
            // once we get the PR we care about we're done here - if we keep getting updates then
            // for every new pr this method will be triggered again (since its subscribed to the same
            // observable that's being pushed to in subscribe) and we'll get buffer overflow exceptions.
            subject.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .take(1)
                .subscribe(pr -> {
                    if (pr.getWeight() < weightSet.getWeight() || pr.getId().equals(weightSet.getId()))
                    {
                        pushToSubscriber(weightSet, exercise);
                    }
                });
        }
    }
}
