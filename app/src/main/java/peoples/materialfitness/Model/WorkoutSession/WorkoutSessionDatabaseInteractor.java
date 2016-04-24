package peoples.materialfitness.Model.WorkoutSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutSessionDatabaseInteractor extends ModelDatabaseInteractor<WorkoutSession>
{
    private static final String TAG = WorkoutSessionDatabaseInteractor.class.getSimpleName();

    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public WorkoutSessionDatabaseInteractor()
    {
        mContext = MaterialFitnessApplication.getApplication();
        mHelper = FitnessDatabaseHelper.getInstance(mContext);
        ;
    }

    @Override
    public Observable<WorkoutSession> save(final WorkoutSession entity)
    {
        return Observable.create(subscriber -> {
            if (!subscriber.isUnsubscribed())
            {
                ContentValues contentValues = entity.getContentValues();

                if (entity.getId() == INVALID_ID)
                {
                    contentValues.remove(BaseColumns._ID);
                }

                entity.setId(mHelper.getDatabase().insertWithOnConflict(WorkoutSessionContract.TABLE_NAME,
                        null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
                subscriber.onNext(entity);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void delete(WorkoutSession entity)
    {
        String WHERE_CLAUSE = WorkoutSessionContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(entity.getId())};
        mHelper.getDatabase().delete(ExerciseSessionContract.TABLE_NAME,
                WHERE_CLAUSE, ARGS);
    }

    public Observable<WorkoutSession> getTodaysWorkoutSession()
    {
        return getDatesWorkoutSession(DateUtils.getTodaysDate().getTime());
    }

    public Observable<WorkoutSession> getDatesWorkoutSession(long millisSinceEpoch)
    {
        long midnightDate = DateUtils.getDatesMidnightTime(millisSinceEpoch);
        long endOfDayDate = DateUtils.getDatesEndOfDayTime(millisSinceEpoch);

        String whereClause = WorkoutSessionContract.COLUMN_NAME_DATE + " >= ? AND " + WorkoutSessionContract.COLUMN_NAME_DATE + " <= ?";
        String[] arguments = new String[]{String.valueOf(midnightDate), String.valueOf(endOfDayDate)};

        return fetchWithClause(whereClause, arguments);
    }

    @Override
    public Observable<WorkoutSession> fetchWithArguments(final String whereClause,
                                                         final String[] args,
                                                         final String groupBy,
                                                         final String[] columns,
                                                         final String having,
                                                         final String orderBy,
                                                         final String limit)
    {
        return FitnessDatabaseUtils.getCursorObservable(WorkoutSessionContract.TABLE_NAME,
                whereClause, args, groupBy, columns, having, orderBy, limit, mContext)
                .flatMap(this::getWorkoutSessionFromCursor);
    }

    @Override
    public Observable<WorkoutSession> cascadeSave(WorkoutSession entity)
    {
        // First save ourselves.
        return save(entity)
                .flatMap(workoutSession -> {
                    // Now save all of our exercise sessions.
                    ExerciseSessionDatabaseInteractor interactor = new ExerciseSessionDatabaseInteractor();
                    for (ExerciseSession session : entity.getExercises())
                    {
                        session.setWorkoutSessionId(entity.getId());
                        interactor.cascadeSave(session).subscribe();
                    }
                    Log.d(TAG, "Saving workout session...");
                    return Observable.just(workoutSession);
                });
    }

    @Override
    public void cascadeDelete(WorkoutSession entity)
    {
        // First delete ourselves
        delete(entity);
        // Now delete all of our exercise sessions.
        ExerciseSessionDatabaseInteractor interactor = new ExerciseSessionDatabaseInteractor();
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
        Observable<List<ExerciseSession>> exerciseObservable = new ExerciseSessionDatabaseInteractor()
                .fetchWithClause(setWhereClause, setArguments)
                .toList();

        return exerciseObservable
                .map(exerciseSessions -> WorkoutSession.getWorkoutSession(contentValues, exerciseSessions));
    }

    public Observable<WorkoutSession> getWorkoutSessionsByDate(Ordering ordering, int limit)
    {
        String orderingString = WorkoutSessionContract.COLUMN_NAME_DATE + " " + ordering.toString();
        String limitString = limit == 0 ? null : String.valueOf(limit);
        return fetchWithArguments(null, null, null, null, null, orderingString, limitString);
    }

    public Observable<WorkoutSession> cascadeSaveWorkoutSessions(final List<WorkoutSession> workoutSessions)
    {
        // TODO: I don't think we want to add these modifiers (doOnSubscribe and on terminate) since
        // they're very likely to be overwritten forward up the chain.
        return Observable.from(workoutSessions)
                .flatMap(this::cascadeSave)
                .doOnSubscribe(() -> mHelper.getDatabase().beginTransaction())
                .doOnTerminate(() -> {
                    mHelper.getDatabase().setTransactionSuccessful();
                    mHelper.getDatabase().endTransaction();
                });
    }
}
