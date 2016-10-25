package peoples.materialfitness.Model;

import android.content.Context;
import android.database.Cursor;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by Alex Sullivan on 3/5/16.
 */
public final class FitnessDatabaseUtils
{

    public static Observable<Cursor> getCursorObservable(final String tableName,
                                                         final String whereClause,
                                                         final String[] args,
                                                         final String groupBy,
                                                         final String[] columns,
                                                         final String having,
                                                         final String orderBy,
                                                         final String limit,
                                                         Context context)
    {
        FitnessDatabaseHelper helper = FitnessDatabaseHelper.getInstance(context);;
        
        return Observable.create((Observable.OnSubscribe<Cursor>) subscriber ->
        {
            if (!subscriber.isUnsubscribed())
            {
                Cursor cursor = helper.getDatabase().query(tableName,
                        columns, whereClause, args, groupBy, having, orderBy, limit);
                while (cursor.moveToNext())
                {
                    subscriber.onNext(cursor);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).onBackpressureBuffer();
    }
}
