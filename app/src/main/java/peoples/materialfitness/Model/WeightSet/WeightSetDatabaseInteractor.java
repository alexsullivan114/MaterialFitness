package peoples.materialfitness.Model.WeightSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WeightSetDatabaseInteractor extends ModelDatabaseInteractor<WeightSet>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public WeightSetDatabaseInteractor()
    {
        mContext = MaterialFitnessApplication.getApplication();
        mHelper = FitnessDatabaseHelper.getInstance(mContext);;
    }

    @Override
    public Observable<WeightSet> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<WeightSet> save(WeightSet entity)
    {
        final String WHERE = ExerciseSessionContract._ID + " = ?";
        final String[] ARGS = new String[]{String.valueOf(entity.getExerciseSessionId())};

        return new ExerciseSessionDatabaseInteractor()
                .fetchWithClause(WHERE, ARGS)
                .map(ExerciseSession::getExercise)
                .flatMap(this::getPrForExercise)
                .toList()
                .flatMap(weightSets -> {
                    if (weightSets.isEmpty())
                    {
                        entity.setIsPr(true);
                        return performWeightSetSave(entity);
                    }
                    else
                    {
                        WeightSet pr = weightSets.get(0);
                        if (entity.getWeight() > pr.getWeight())
                        {
                            entity.setIsPr(true);
                            pr.setIsPr(false);
                        }

                        return performWeightSetSave(pr)
                                .flatMap(weightSet -> performWeightSetSave(entity));
                    }
                });
    }

    @Override
    public Observable<WeightSet> fetchWithParentId(long parentId)
    {
        final String WHERE = WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID + " = ? ";
        final String[] ARGS = new String[]{String.valueOf(parentId)};
        return fetchWithClause(WHERE, ARGS);
    }

    private Observable<WeightSet> performWeightSetSave(final WeightSet weightSet)
    {
        return Observable.create(subscriber -> {

            if (!subscriber.isUnsubscribed())
            {
                Log.i(TAG, "Saving weight set...");
                ContentValues contentValues = weightSet.getContentValues();

                if (weightSet.getId() == INVALID_ID)
                {
                    contentValues.remove(BaseColumns._ID);
                }

                weightSet.setId(mHelper.getDatabase().insertWithOnConflict(WeightSetContract.TABLE_NAME,
                                                                        null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
                subscriber.onNext(weightSet);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> delete(final WeightSet entity)
    {
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            if (!subscriber.isUnsubscribed())
            {
                String WHERE_CLAUSE = WeightSetContract._ID + " = ?";
                String[] ARGS = new String[]{String.valueOf(entity.getId())};
                subscriber.onNext(mHelper.getDatabase().delete(WeightSetContract.TABLE_NAME,
                                                               WHERE_CLAUSE, ARGS) != 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> deleteWithPrCheck(final WeightSet entity, final Exercise exercise)
    {
        return delete(entity)
                .flatMap(result -> {
                    if (entity.getIsPr())
                    {
                        return recalculatePrs(exercise);
                    }
                    else
                    {
                        return Observable.just(result);
                    }
                });
    }

    private Observable<Boolean> recalculatePrs(final Exercise exercise)
    {
        final String WHERE = ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID + " = ?";
        final String[] ARGS = new String[]{String.valueOf(exercise.getId())};

        return new ExerciseSessionDatabaseInteractor()
                .fetchWithClause(WHERE, ARGS)
                .map(ExerciseSession::getSets)
                .flatMap(Observable::from)
                .toSortedList(new Func2<WeightSet, WeightSet, Integer>()
                {
                    @Override
                    public Integer call(WeightSet weightSet, WeightSet weightSet2)
                    {
                       return weightSet.getWeight() - weightSet2.getWeight();
                    }
                })
                .map(weightSets -> {
                    WeightSet pr = weightSets.get(weightSets.size() - 1);
                    pr.setIsPr(true);
                    return pr;
                })
                .flatMap(prSet -> new WeightSetDatabaseInteractor().save(prSet))
                .map(savedPr -> savedPr.getId() != INVALID_ID);
    }

    @Override
    public Observable<WeightSet> cascadeSave(WeightSet entity)
    {
        return this.save(entity);
    }

    @Override
    public Observable<Boolean> cascadeDelete(WeightSet entity)
    {
        return delete(entity);
    }

    @Override
    public Observable<WeightSet> fetchWithArguments(final String whereClause,
                                                    final String[] args,
                                                    final String groupBy,
                                                    final String[] columns,
                                                    final String having,
                                                    final String orderBy,
                                                    final String limit)
    {
        return FitnessDatabaseUtils.getCursorObservable(WeightSetContract.TABLE_NAME,
                whereClause, args, groupBy, columns, having, orderBy, limit, mContext)
                .map(cursor -> {
                    ContentValues contentValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                    return WeightSet.getWeightSet(contentValues);
                });
    }

    public Observable<WeightSet> getPrForExercise(final Exercise exercise)
    {
        final String WHERE = ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID + " = ?";
        final String[] ARGS = new String[]{String.valueOf(exercise.getId())};

        return new ExerciseSessionDatabaseInteractor().fetchWithClause(WHERE, ARGS)
                .map(ExerciseSession::getSets)
                .flatMap(Observable::from)
                .filter(WeightSet::getIsPr);
    }
}
