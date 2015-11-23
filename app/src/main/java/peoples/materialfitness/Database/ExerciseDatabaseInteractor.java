package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;

/**
 * Created by Alex Sullivan on 10/18/2015.
 *
 * Interactor for the {@link peoples.materialfitness.Database.Exercise} object.
 * Handled CRUD operations for the exercise object.
 */
public class ExerciseDatabaseInteractor implements ModelDatabaseInteractor<Exercise>
{
    @Override
    public Observable<Exercise> fetchAll()
    {
        return Observable.from(Exercise.listAll(Exercise.class));
    }

    @Override
    public Observable<Exercise> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.from(Exercise.find(Exercise.class, whereClause, arguments));
    }

    @Override
    public void save(Exercise exercise)
    {
        exercise.save();
    }

    @Override
    public void delete(Exercise exercise)
    {
        exercise.delete();
    }
}
