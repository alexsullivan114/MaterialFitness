package peoples.materialfitness.Model.WeightSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.Cache.DatabasePrCache;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import rx.Observable;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WeightSetDatabaseInteractor extends ModelDatabaseInteractor<WeightSet>
{
    private final Context context;
    private final FitnessDatabaseHelper helper;

    public WeightSetDatabaseInteractor()
    {
        context = MaterialFitnessApplication.getApplication();
        helper = FitnessDatabaseHelper.getInstance(context);
    }

    @Override
    public Observable<WeightSet> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    /**
     * Edits the provided weight set - assumed values have been updated and ID is still the same.
     * @param weightSet The (already ID'd) weightset to update
     * @return The newly saved weightset
     */
    public Observable<WeightSet> edit(final WeightSet weightSet,
                                      final Exercise exercise)
    {
        return saveWithPrUpdates(weightSet, exercise);
    }

    @Override
    public Observable<WeightSet> save(WeightSet entity)
    {
        return Observable.defer(() -> {
            Log.i(TAG, "Saving weight set...");
            ContentValues contentValues = entity.getContentValues();

            if (entity.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            entity.setId(helper.getDatabase().insertWithOnConflict(WeightSetContract.TABLE_NAME,
                                                                      null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
            return Observable.just(entity);
        });
    }

    public Observable<WeightSet> saveWithPrUpdates(final WeightSet entity,
                                                   final Exercise exercise)
    {
        return save(entity)
                .doOnNext(weightSet -> DatabasePrCache.getInstance().weightSetAdded(entity, exercise));
    }

    @Override
    public Observable<WeightSet> fetchWithParentId(long parentId)
    {
        final String WHERE = WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID + " = ? ";
        final String[] ARGS = new String[]{String.valueOf(parentId)};
        return fetchWithClause(WHERE, ARGS);
    }


    @Override
    public Observable<Boolean> delete(final WeightSet entity)
    {
        return Observable.defer(() -> {
            String WHERE_CLAUSE = WeightSetContract._ID + " = ?";
            String[] ARGS = new String[]{String.valueOf(entity.getId())};
            return Observable.just(helper.getDatabase().delete(WeightSetContract.TABLE_NAME,
                                                          WHERE_CLAUSE, ARGS) != 0);
        });
    }

    public Observable<Boolean> deleteWithPrUpdates(final WeightSet entity,
                                                   final Exercise exercise)
    {
        return delete(entity)
                .doOnNext(weightSet -> DatabasePrCache.getInstance().weightSetModified(entity, exercise));
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
                                                        whereClause, args, groupBy, columns, having, orderBy, limit, context)
                .map(cursor -> {
                    ContentValues contentValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                    return WeightSet.getWeightSet(contentValues);
                });
    }
}
