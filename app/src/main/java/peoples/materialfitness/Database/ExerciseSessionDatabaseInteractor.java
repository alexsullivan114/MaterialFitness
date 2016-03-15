package peoples.materialfitness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class ExerciseSessionDatabaseInteractor implements ModelDatabaseInteractor<ExerciseSession>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public ExerciseSessionDatabaseInteractor(Context context)
    {
        mContext = context.getApplicationContext();
        mHelper = new FitnessDatabaseHelper(mContext);
    }

    @Override
    public Observable<ExerciseSession> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<ExerciseSession> fetchWithClause(String whereClause, String[] arguments)
    {

        return FitnessDatabaseUtils.getCursorObservable(ExerciseSessionContract.TABLE_NAME,
                whereClause, arguments, mContext)
                .flatMap(this::getExerciseSessionFromCursor);
    }

    @Override
    public Observable<Long> cascadeSave(ExerciseSession entity)
    {
        // First save ourselves.
        return save(entity)
                .subscribeOn(Schedulers.io())
                .flatMap(id -> {
                    // Now save all of our sets.
                    WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor(mContext);
                    for (WeightSet set : entity.getSets())
                    {
                        set.setExerciseSessionId(entity.getId());
                        interactor.save(set).subscribe();
                    }

                    return Observable.just(id);
                });
    }

    @Override
    public void cascadeDelete(ExerciseSession entity)
    {
        // First delete ourselves
        delete(entity);
        // Now delete all of our sets.
        WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor(mContext);
        for (WeightSet set : entity.getSets())
        {
            interactor.delete(set);
        }
    }

    @Override
    public Observable<Long> save(ExerciseSession entity)
    {
        return Observable.create(subscriber -> {
            ContentValues contentValues = entity.getContentValues();

            if (entity.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            entity.setId(mHelper.getReadableDatabase().insertWithOnConflict(ExerciseSessionContract.TABLE_NAME,
                    null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
            mHelper.getReadableDatabase().close();

            subscriber.onNext(entity.getId());
            subscriber.onCompleted();
        });
    }
    @Override
    public void delete(ExerciseSession entity)
    {
        String WHERE_CLAUSE = ExerciseSessionContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(entity.getId())};
        mHelper.getReadableDatabase().delete(ExerciseSessionContract.TABLE_NAME,
                WHERE_CLAUSE, ARGS);
        mHelper.getReadableDatabase().close();
    }

    private Observable<ExerciseSession> getExerciseSessionFromCursor(Cursor cursor)
    {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        // Build up our weight sets for this exercise session
        String setWhereClause = WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID + " = ?";
        String[] setArguments = new String[]{contentValues.getAsString(ExerciseSessionContract._ID)};
        Observable<List<WeightSet>> setsObservable = new WeightSetDatabaseInteractor(mContext)
                .fetchWithClause(setWhereClause, setArguments)
                .toList();

        // Build up our Exercise for this exercise session
        String exerciseWhereClause = ExerciseContract._ID + " = ?";
        String[] exerciseArguments = new String[]{contentValues.getAsString(ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID)};
        Observable<Exercise> exerciseObservable = new ExerciseDatabaseInteractor(mContext)
                .fetchWithClause(exerciseWhereClause, exerciseArguments);

        return Observable.zip(setsObservable, exerciseObservable,
                (weightSetList, exercise) -> ExerciseSession.getExerciseSession(contentValues, exercise, weightSetList));
    }
}
