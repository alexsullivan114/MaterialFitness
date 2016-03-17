package peoples.materialfitness.Model.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 10/18/2015.
 *
 * Interactor for the {@link Exercise} object.
 * Handled CRUD operations for the exercise object.
 */
public class ExerciseDatabaseInteractor implements ModelDatabaseInteractor<Exercise>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public ExerciseDatabaseInteractor(Context context)
    {
        mContext = context.getApplicationContext();
        mHelper = FitnessDatabaseHelper.getInstance(mContext);
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
               Cursor cursor = mHelper.getDatabase().query(ExerciseContract.TABLE_NAME,
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
    public Observable<Long> save(Exercise exercise)
    {
        return Observable.create(subscriber -> {

            ContentValues contentValues = exercise.getContentValues();

            if (exercise.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            long updatedId = mHelper.getDatabase().insert(ExerciseContract.TABLE_NAME,
                    null, contentValues);
            exercise.setId(updatedId);
            subscriber.onNext(updatedId);
            subscriber.onCompleted();

        });
    }

    @Override
    public void delete(Exercise exercise)
    {
        String whereClause = ExerciseContract._ID + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getId())};

        mHelper.getDatabase().delete(ExerciseContract.TABLE_NAME,
                whereClause, arguments);
    }

    /**
     * Perform a unique save of this exercise. Current ORM doesn't support unique columns.
     * @param exercise
     */
    public Observable<Long> uniqueSaveExercise(Exercise exercise)
    {

        String whereClause = ExerciseContract.COLUMN_NAME_TITLE + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getTitle())};

        return fetchWithClause(whereClause, arguments)
                .subscribeOn(Schedulers.io())
                .toList()
                .distinct()
                .flatMap(exercises -> {
                    if (exercises.size() > 0)
                    {
                        return Observable.just(exercises.get(0).getId());
                    }
                    else
                    {
                        return save(exercise);
                    }
                });
    }

    @Override
    public Observable<Long> cascadeSave(Exercise entity)
    {
        return this.save(entity);
    }

    @Override
    public void cascadeDelete(Exercise entity)
    {
        // no-op
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
