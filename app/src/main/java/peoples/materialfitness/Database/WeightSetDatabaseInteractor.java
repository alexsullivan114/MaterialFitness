package peoples.materialfitness.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WeightSetDatabaseInteractor implements ModelDatabaseInteractor<WeightSet>
{
    private final Context mContext;
    private final FitnessDatabaseHelper mHelper;

    public WeightSetDatabaseInteractor(Context context)
    {
        mContext = context.getApplicationContext();
        mHelper = new FitnessDatabaseHelper(mContext);
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
    public void save(WeightSet entity)
    {
        Observable.create(subscriber -> {

            ContentValues contentValues = entity.getContentValues();

            if (entity.getId() == INVALID_ID)
            {
                contentValues.remove(BaseColumns._ID);
            }

            entity.setId(mHelper.getReadableDatabase().insertWithOnConflict(WeightSetContract.TABLE_NAME,
                    null, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void delete(WeightSet entity)
    {
        String WHERE_CLAUSE = WeightSetContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(entity.getId())};
        mHelper.getReadableDatabase().delete(ExerciseSessionContract.TABLE_NAME,
                WHERE_CLAUSE, ARGS);
    }

    @Override
    public void cascadeSave(WeightSet entity)
    {
        // no-op
    }

    @Override
    public void cascadeDelete(WeightSet entity)
    {
        // no-op
    }
}
