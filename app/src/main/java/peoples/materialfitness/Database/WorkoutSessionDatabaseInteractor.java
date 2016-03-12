package peoples.materialfitness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutSessionDatabaseInteractor implements ModelDatabaseInteractor<WorkoutSession>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public WorkoutSessionDatabaseInteractor(Context context)
    {
        mContext = context.getApplicationContext();
        mHelper = new FitnessDatabaseHelper(mContext);
    }

    @Override
    public Observable<WorkoutSession> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<WorkoutSession> fetchWithClause(String whereClause, String[] arguments)
    {
        return FitnessDatabaseUtils.getCursorObservable(WorkoutSessionContract.TABLE_NAME,
                whereClause, arguments, mContext)
                .flatMap(this::getWorkoutSessionFromCursor);
    }

    @Override
    public void save(final WorkoutSession entity)
    {
        Observable.create(subscriber -> {
            mHelper.getReadableDatabase().insertWithOnConflict(WorkoutSessionContract.TABLE_NAME,
                    null, entity.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void delete(WorkoutSession entity)
    {
        String WHERE_CLAUSE = WorkoutSessionContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(entity.getId())};
        mHelper.getReadableDatabase().delete(ExerciseSessionContract.TABLE_NAME,
                WHERE_CLAUSE, ARGS);
    }

    public Observable<WorkoutSession> getTodaysWorkoutSession()
    {
        return getDatesWorkoutSession(DateUtils.getTodaysDate().getTime());
    }

    public Observable<WorkoutSession> getDatesWorkoutSession(long millisSinceEpoch)
    {
        String whereClause = WorkoutSessionContract.COLUMN_NAME_DATE + " = ?";
        String[] arguments = new String[]{String.valueOf(millisSinceEpoch)};

        return fetchWithClause(whereClause, arguments);
    }

    @Override
    public void cascadeSave(WorkoutSession entity)
    {
        // First save ourselves.
        save(entity);
        // Now save all of our exercise sessions.
        ExerciseSessionDatabaseInteractor interactor = new ExerciseSessionDatabaseInteractor(mContext);
        for (ExerciseSession session : entity.getExercises())
        {
            session.setWorkoutSessionId(entity.getId());
            interactor.save(session);
        }
    }

    @Override
    public void cascadeDelete(WorkoutSession entity)
    {
        // First delete ourselves
        delete(entity);
        // Now delete all of our exercise sessions.
        ExerciseSessionDatabaseInteractor interactor = new ExerciseSessionDatabaseInteractor(mContext);
        for (ExerciseSession session : entity.getExercises())
        {
            interactor.delete(session);
        }
    }

    private Observable<WorkoutSession> getWorkoutSessionFromCursor(Cursor cursor)
    {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        // Build up our exercises for this exercise session
        String setWhereClause = ExerciseSessionContract.COLUMN_NAME_WORKOUT_SESSION_ID + " = ?";
        String[] setArguments = new String[]{contentValues.getAsString(WorkoutSessionContract._ID)};
        Observable<List<ExerciseSession>> exerciseObservable = new ExerciseSessionDatabaseInteractor(mContext)
                .fetchWithClause(setWhereClause, setArguments)
                .toList();

        return exerciseObservable
                .map(exerciseSessions -> WorkoutSession.getWorkoutSession(contentValues, exerciseSessions));
    }
}
