package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class ExerciseSessionDatabaseInteractor implements ModelDatabaseInteractor<ExerciseSession>
{
    @Override
    public Observable<ExerciseSession> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<ExerciseSession> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.empty();
//        return Observable.from(ExerciseSession.find(ExerciseSession.class, whereClause, arguments))
//                .flatMap(this::associateExerciseSessionSets);

    }

    @Override
    public void cascadeSave(ExerciseSession entity)
    {

    }

    @Override
    public void cascadeDelete(ExerciseSession entity)
    {

    }

    @Override
    public void save(ExerciseSession entity)
    {
//        // Rxjava just makes threading so fun.
//        Observable.just(entity)
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    // Save the exercise session.
//                    entity.save();
//                    // Save each rep associated with this workout session.
//                    WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();
//                    for (WeightSet mapping : entity.getSets())
//                    {
//                        mapping.setExerciseSessionId(entity.getId());
//                        interactor.save(mapping);
//                    }
//                });
    }

    @Override
    public void delete(ExerciseSession entity)
    {
//        entity.delete();
    }
}
