package peoples.materialfitness.Model;

import android.content.Context;
import android.database.Cursor;

import rx.Observable;

/**
 * Created by Alex Sullivan on 3/5/16.
 */
public final class FitnessDatabaseUtils
{

    public static Observable<Cursor> getCursorObservable(final String tableName,
                                                         final String whereClause,
                                                         final String[] args,
                                                         Context context)
    {
        FitnessDatabaseHelper helper = FitnessDatabaseHelper.getInstance(context);;

        return Observable.create((Observable.OnSubscribe<Cursor>) subscriber ->
        {
            if (!subscriber.isUnsubscribed())
            {
                Cursor cursor = helper.getDatabase().query(tableName,
                        null, whereClause, args, null, null, null);
                while (cursor.moveToNext())
                {
                    subscriber.onNext(cursor);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }
}
