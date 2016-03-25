package peoples.materialfitness.Model.WeightSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import rx.Observable;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WeightSetDatabaseInteractor implements ModelDatabaseInteractor<WeightSet>
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
    public Observable<WeightSet> fetchWithClause(String whereClause, String[] arguments)
    {
        return FitnessDatabaseUtils.getCursorObservable(WeightSetContract.TABLE_NAME,
                whereClause, arguments, mContext)
                .map(cursor -> {
                    ContentValues contentValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                    return WeightSet.getWeightSet(contentValues);
                });

    }

    @Override
    public Observable<Long> save(WeightSet entity)
    {
        return Observable.create(subscriber -> {

            ContentValues contentValues = entity.getContentValues();

            if (entity.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            entity.setId(mHelper.getDatabase().insertWithOnConflict(WeightSetContract.TABLE_NAME,
                    null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
            subscriber.onNext(entity.getId());
            subscriber.onCompleted();
        });
    }

    @Override
    public void delete(WeightSet entity)
    {
        String WHERE_CLAUSE = WeightSetContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(entity.getId())};
        mHelper.getDatabase().delete(ExerciseSessionContract.TABLE_NAME,
                WHERE_CLAUSE, ARGS);
    }

    @Override
    public Observable<Long> cascadeSave(WeightSet entity)
    {
        return this.save(entity);
    }

    @Override
    public void cascadeDelete(WeightSet entity)
    {
        // no-op
    }
}
