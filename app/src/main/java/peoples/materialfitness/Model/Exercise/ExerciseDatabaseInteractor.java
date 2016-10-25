package peoples.materialfitness.Model.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSetContract;
import rx.Observable;

/**
 * Created by Alex Sullivan on 10/18/2015.
 *
 * Interactor for the {@link Exercise} object.
 * Handled CRUD operations for the exercise object.
 */
public class ExerciseDatabaseInteractor extends ModelDatabaseInteractor<Exercise>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public ExerciseDatabaseInteractor()
    {
        super();
        mContext = MaterialFitnessApplication.getApplication();
        mHelper = FitnessDatabaseHelper.getInstance(mContext);
    }

    @Override
    public Observable<Exercise> fetchWithClause(String whereClause, String[] arguments)
    {
        return fetchWithArguments(whereClause, arguments, null, null, null, null, null);
    }

    @Override
    public Observable<Exercise> save(Exercise exercise)
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
            subscriber.onNext(exercise);
            subscriber.onCompleted();

        });
    }

    @Override
    public Observable<Boolean> delete(Exercise exercise)
    {
        return Observable.create(subscriber -> {
                if (!subscriber.isUnsubscribed())
                {
                    String whereClause = ExerciseContract._ID + " = ?";
                    String[] arguments = new String[]{String.valueOf(exercise.getId())};

                    subscriber.onNext(mHelper.getDatabase().delete(ExerciseContract.TABLE_NAME,
                                                 whereClause, arguments) != 0);
                    subscriber.onCompleted();
                }
            });
    }

    /**
     * Perform a unique save of this exercise. Current ORM doesn't support unique columns.
     * @param exercise
     */
    public Observable<Exercise> uniqueSaveExercise(Exercise exercise)
    {

        String whereClause = ExerciseContract.COLUMN_NAME_TITLE + " = ?";
        String[] arguments = new String[]{String.valueOf(exercise.getTitle())};

        return fetchWithClause(whereClause, arguments)
                .toList()
                .distinct()
                .flatMap(exercises -> {
                    if (exercises.size() > 0)
                    {
                        return Observable.just(exercises.get(0));
                    }
                    else
                    {
                        return save(exercise);
                    }
                });
    }

    @Override
    public Observable<Exercise> fetchWithParentId(long parentId)
    {
        throw new RuntimeException("Cannot fetch an exercise with any parent id!");
    }

    @Override
    public Observable<Exercise> cascadeSave(Exercise entity)
    {
        return this.save(entity);
    }

    @Override
    public Observable<Boolean> cascadeDelete(Exercise entity)
    {
        return delete(entity);
    }

    @Override
    public Observable<Exercise> fetchWithArguments(final String whereClause,
                                                         final String[] args,
                                                         final String groupBy,
                                                         final String[] columns,
                                                         final String having,
                                                         final String orderBy,
                                                         final String limit)
    {
        return FitnessDatabaseUtils.getCursorObservable(ExerciseContract.TABLE_NAME,
                whereClause, args, groupBy, columns, having, orderBy, limit, mContext)
                .map(this::getExerciseFromCursor);
    }

    public Observable<Exercise> fetchWithId(final long id)
    {
        final String WHERE = ExerciseContract._ID + " = ? ";
        final String[] ARGS = new String[]{String.valueOf(id)};
        return fetchWithClause(WHERE, ARGS);
    }

    private Exercise getExerciseFromCursor(Cursor cursor)
    {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        return Exercise.getExercise(contentValues);
    }
}
