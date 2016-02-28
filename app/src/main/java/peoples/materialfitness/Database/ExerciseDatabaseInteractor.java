package peoples.materialfitness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
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
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public ExerciseDatabaseInteractor(Context context)
    {
        mContext = context.getApplicationContext();
        mHelper = new FitnessDatabaseHelper(mContext);
    }

    @Override
    public Observable<Exercise> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<Exercise> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.create(subscriber -> {
           if (!subscriber.isUnsubscribed())
           {
               Cursor cursor = mHelper.getReadableDatabase().query(ExerciseContract.TABLE_NAME,
                       null, whereClause, arguments, null, null, null);

               while (cursor.moveToNext())
               {
                   Exercise exercise = getExerciseFromCursor(cursor);
                   subscriber.onNext(exercise);
               }

               subscriber.onCompleted();
           }
        });
    }

    @Override
    public void save(Exercise exercise)
    {
        long updatedId = mHelper.getReadableDatabase().insert(ExerciseContract.TABLE_NAME,
                null, exercise.getContentValues());
        exercise.setId(updatedId);
    }

    @Override
    public void delete(Exercise exercise)
    {
        String whereClause = ExerciseContract._ID + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getId())};

        mHelper.getReadableDatabase().delete(ExerciseContract.TABLE_NAME,
                whereClause, arguments);
    }

    /**
     * Perform a unique save of this exercise. Current ORM doesn't support unique columns.
     * @param exercise
     */
    public void uniqueSaveExercise(Exercise exercise)
    {

        String whereClause = ExerciseContract.COLUMN_NAME_TITLE + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getTitle())};

        fetchWithClause(whereClause, arguments)
                .subscribeOn(Schedulers.io())
                .map(Exercise::getTitle)
                .toList()
                .distinct()
                .subscribe(values -> {
                    if (!values.contains(exercise.getTitle()))
                    {
                        this.save(exercise);
                    }
                });
    }

    @Override
    public void cascadeSave(Exercise entity)
    {

    }

    @Override
    public void cascadeDelete(Exercise entity)
    {

    }

    private List<Exercise> getExercisesFromCursor(Cursor cursor)
    {
        ArrayList<Exercise> exercises = new ArrayList<>();

        while (cursor.moveToNext())
        {
            exercises.add(getExerciseFromCursor(cursor));
        }

        return exercises;
    }

    private Exercise getExerciseFromCursor(Cursor cursor)
    {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        return Exercise.getExercise(contentValues);
    }
}
