package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public Observable<List<String>> getExerciseTitles()
    {
        return  fetchAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .map(Exercise::getTitle)
                .toList()
                .distinct();
    }

    /**
     * Perform a unique save of this exercise. Current ORM doesn't support unique columns.
     * @param exercise
     */
    public void uniqueSaveExercise(Exercise exercise)
    {

        String whereClause = Exercise.TITLE_COLUMN + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getTitle())};

        new ExerciseDatabaseInteractor().fetchWithClause(whereClause, arguments)
                .subscribeOn(Schedulers.newThread())
                .map(Exercise::getTitle)
                .toList()
                .distinct()
                .subscribe(values -> {
                    if (!values.contains(exercise.getTitle()))
                    {
                        exercise.save();
                    }
                });
    }
}
