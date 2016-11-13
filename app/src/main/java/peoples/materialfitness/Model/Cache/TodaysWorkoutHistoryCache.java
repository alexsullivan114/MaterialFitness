package peoples.materialfitness.Model.Cache;

import android.util.Log;

import java.util.List;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 10/16/2016.
 *
 * TODO: I dont think this cache should save values to the database, the database should push
 * updates to this cache.
 *
 * TODO: I think this cache needs to be more reactive in nature - it should expose an observable
 * for a workout session that gets updates pushed to it.
 */

public class TodaysWorkoutHistoryCache
{
    private static final String TAG = TodaysWorkoutHistoryCache.class.getSimpleName();

    private static TodaysWorkoutHistoryCache INSTANCE;
    private WorkoutSession todaysWorkoutSession;
    // The observable that's currently being used to create a new instance of a workout session for
    // today. This variable exists so we don't accidentally create two instances of a workout session
    // for today.
    private Observable<WorkoutSession> creationObservable;

    private TodaysWorkoutHistoryCache() {}

    public static TodaysWorkoutHistoryCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new TodaysWorkoutHistoryCache();
        }

        return INSTANCE;
    }

    public Observable<WorkoutSession> getTodaysWorkoutSession()
    {
        if (todaysWorkoutSession == null)
        {
            if (creationObservable == null)
            {
                creationObservable = createTodaysWorkoutSession();
            }

            return creationObservable;
        }
        else
        {
            return Observable.just(todaysWorkoutSession);
        }
    }

    private Observable<WorkoutSession> createTodaysWorkoutSession()
    {
        return new WorkoutSessionDatabaseInteractor()
                .getTodaysWorkoutSession()
                .toList()
                .flatMap(workoutSessions -> {
                   if (workoutSessions.size() == 0)
                   {
                       WorkoutSession workoutSession = new WorkoutSession(DateUtils.getTodaysDate().getTime());
                       return new WorkoutSessionDatabaseInteractor().save(workoutSession);
                   }
                   else
                   {
                       return Observable.from(workoutSessions);
                   }
                })
                .doOnNext(workoutSession -> todaysWorkoutSession = workoutSession);
    }

    public void addExerciseSessionToCache(final boolean saveUpdate,
                                          final ExerciseSession exerciseSession)
    {

        getTodaysWorkoutSession()
                .observeOn(Schedulers.io())
                .subscribe(workoutSession -> {
                    workoutSession.uniqueAddExerciseSession(exerciseSession);
                    if (saveUpdate)
                    {
                        new ExerciseSessionDatabaseInteractor().cascadeSave(exerciseSession).subscribe();
                    }
                });
    }

    public void addSets(final boolean saveUpdate,
                       final List<WeightSet> newSets)
    {
        getTodaysWorkoutSession()
                .observeOn(Schedulers.io())
                .subscribe(workoutSession -> {
                    final WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();
                    for (WeightSet set : newSets)
                    {
                        for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
                        {
                            if (exerciseSession.getId() == set.getExerciseSessionId())
                            {
                                exerciseSession.addSet(set);

                                if (saveUpdate)
                                {
                                    interactor.saveWithPrUpdates(set, exerciseSession.getExercise()).subscribe();
                                }
                            }
                        }
                    }
                }, (throwable -> {
                    Log.e(TAG, throwable.toString());
                }));
    }

    public void deleteExerciseSession(final boolean saveUpdate,
                                      final ExerciseSession exerciseSession)
    {
        getTodaysWorkoutSession()
                .observeOn(Schedulers.io())
                .subscribe(workoutSession -> {
                    if (saveUpdate)
                    {
                        new ExerciseSessionDatabaseInteractor().delete(exerciseSession).subscribe();
                    }
                });
    }

    public void deleteSets(final boolean saveUpdate,
                           final List<WeightSet> sets)
    {
        getTodaysWorkoutSession()
                .observeOn(Schedulers.io())
                .subscribe(workoutSession -> {
                    for (WeightSet set : sets)
                    {
                        for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
                        {
                            if (exerciseSession.getId() == set.getExerciseSessionId())
                            {
                                exerciseSession.getSets().remove(set);
                            }
                        }
                    }

                    if (saveUpdate)
                    {
                        cascadeSaveWorkoutSession(workoutSession);
                    }
                });
    }

    /**
     * Shorthand to execute a cascading save on a workout session.
     * @param workoutSession Should be the same object as todaysWorkoutSession.
     */
    private void cascadeSaveWorkoutSession(WorkoutSession workoutSession)
    {
        new WorkoutSessionDatabaseInteractor().cascadeSave(workoutSession).subscribe();
    }
}
