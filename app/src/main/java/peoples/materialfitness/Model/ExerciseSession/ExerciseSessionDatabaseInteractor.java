package peoples.materialfitness.Model.ExerciseSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseContract;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetContract;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class ExerciseSessionDatabaseInteractor extends ModelDatabaseInteractor<ExerciseSession>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public ExerciseSessionDatabaseInteractor()
    {
        mContext = MaterialFitnessApplication.getApplication();
        mHelper = FitnessDatabaseHelper.getInstance(mContext);;
    }

    @Override
    public Observable<ExerciseSession> fetchWithArguments(final String whereClause,
                                                   final String[] args,
                                                   final String groupBy,
                                                   final String[] columns,
                                                   final String having,
                                                   final String orderBy,
                                                   final String limit)
    {
        return FitnessDatabaseUtils.getCursorObservable(ExerciseSessionContract.TABLE_NAME,
                whereClause, args, groupBy, columns, having, orderBy, limit, mContext)
                .flatMap(this::getExerciseSessionFromCursor);
    }

    @Override
    public Observable<ExerciseSession> cascadeSave(ExerciseSession entity)
    {
        // First save our exercise if it hasn't been saved...
        // And our exercise if it hasn't been saved...
        ExerciseDatabaseInteractor exerciseInteracor = new ExerciseDatabaseInteractor();
        return exerciseInteracor.uniqueSaveExercise(entity.getExercise())
                .flatMap(exercise -> {
                    entity.getExercise().setId(exercise.getId());
                    Log.i(TAG, "Saving exercise session with exercise: " + exercise.getTitle());
                    return save(entity);
                })
                .flatMap(exerciseSession -> {
                    // Now save all of our sets.
                    WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();
                    for (WeightSet set : entity.getSets())
                    {
                        set.setExerciseSessionId(entity.getId());
                        interactor.save(set).subscribe();
                    }

                    return Observable.just(exerciseSession);
                });
    }

    @Override
    public Observable<Boolean> cascadeDelete(final ExerciseSession entity)
    {
        // First delete ourselves
        return delete(entity)
                .flatMap(result -> {
                    // Check our associated workout session to see if it has any exercise sessions left.
                    final String WHERE = WorkoutSessionContract._ID + " = ?";
                    final String[] ARGS = new String[]{String.valueOf(entity.getWorkoutSessionId())};
                    return new WorkoutSessionDatabaseInteractor().fetchWithClause(WHERE, ARGS);
                })
                .flatMap(workoutSession -> {
                    if (workoutSession.getExercises().size() == 0)
                    {
                        return new WorkoutSessionDatabaseInteractor().delete(workoutSession);
                    }
                    else
                    {
                        return Observable.just(true);
                    }
                })
                .flatMap(result -> Observable.from(entity.getSets()))
                .flatMap(weightSet -> {
                    WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();
                    return interactor.deleteWithPrCheck(weightSet, entity.getExercise());
                });
    }

    @Override
    public Observable<ExerciseSession> fetchWithParentId(long parentId)
    {
        final String WHERE = ExerciseSessionContract.COLUMN_NAME_WORKOUT_SESSION_ID + " = ?";
        final String[] ARGS = new String[]{String.valueOf(parentId)};

        return fetchWithClause(WHERE, ARGS);
    }

    @Override
    public Observable<ExerciseSession> save(ExerciseSession entity)
    {
        return Observable.create(subscriber -> {
            ContentValues contentValues = entity.getContentValues();

            if (entity.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            entity.setId(mHelper.getDatabase().insertWithOnConflict(ExerciseSessionContract.TABLE_NAME,
                    null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));

            subscriber.onNext(entity);
            subscriber.onCompleted();
        });
    }
    @Override
    public Observable<Boolean> delete(ExerciseSession entity)
    {
        return Observable.create(new Observable.OnSubscribe<Boolean>()
        {
            @Override
            public void call(Subscriber<? super Boolean> subscriber)
            {
                if (!subscriber.isUnsubscribed())
                {
                    String WHERE_CLAUSE = ExerciseSessionContract._ID + " = ?";
                    String[] ARGS = new String[]{String.valueOf(entity.getId())};
                    subscriber.onNext(mHelper.getDatabase().delete(ExerciseSessionContract.TABLE_NAME, WHERE_CLAUSE, ARGS) != 0);
                    subscriber.onCompleted();
                }
            }
        });
    }

    private Observable<ExerciseSession> getExerciseSessionFromCursor(Cursor cursor)
    {
        ContentValues contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

        // Build up our weight sets for this exercise session
        String setWhereClause = WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID + " = ?";
        String[] setArguments = new String[]{contentValues.getAsString(ExerciseSessionContract._ID)};
        Observable<List<WeightSet>> setsObservable = new WeightSetDatabaseInteractor()
                .fetchWithClause(setWhereClause, setArguments)
                .toList();

        // Build up our Exercise for this exercise session
        String exerciseWhereClause = ExerciseContract._ID + " = ?";
        String[] exerciseArguments = new String[]{contentValues.getAsString(ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID)};
        Observable<Exercise> exerciseObservable = new ExerciseDatabaseInteractor()
                .fetchWithClause(exerciseWhereClause, exerciseArguments);

        return Observable.zip(setsObservable, exerciseObservable,
                (weightSetList, exercise) -> ExerciseSession.getExerciseSession(contentValues, exercise, weightSetList));
    }

    public Observable<ExerciseSession> getPreviousExerciseSession(Exercise exercise)
    {
        final String WHERE_CLAUSE = ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID + " = ?";
        final String[] WHERE_ARGS = new String[]{String.valueOf(exercise.getId())};
        final WorkoutSessionDatabaseInteractor workoutSessionDatabaseInteractor = new WorkoutSessionDatabaseInteractor();

        return fetchWithClause(WHERE_CLAUSE, WHERE_ARGS)
                .flatMap(exerciseSession -> workoutSessionDatabaseInteractor.fetchWithId(exerciseSession.getWorkoutSessionId()))
                .filter(workoutSession -> !DateUtils.isToday(workoutSession.getWorkoutSessionDate()))
                .toSortedList((workoutSession, workoutSession2) -> {
                    long lhs = workoutSession.getWorkoutSessionDate();
                    long rhs = workoutSession2.getWorkoutSessionDate();
                    return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
                })
                .flatMap((Func1<List<WorkoutSession>, Observable<WorkoutSession>>) Observable::from)
                .takeLast(1)
                .map(WorkoutSession::getExercises)
                .flatMap(Observable::from)
                .filter(exerciseSession1 -> exerciseSession1.getExercise().equals(exercise));
    }
}
