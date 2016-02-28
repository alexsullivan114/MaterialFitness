package peoples.materialfitness.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 2/28/16.
 */
public class FitnessDatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = FitnessDatabaseHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MaterialFitness.db";

    private Context mContext;
    private SQLiteDatabase mDb;

    public FitnessDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Run through and apply all updates that exist.
        mDb = db;
        applyUpdates();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    private void applyUpdates()
    {
        InputStream stream = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;

        try
        {
            for (int i = 1; i <= DATABASE_VERSION; i++)
            {
                int resID = mContext.getResources().getIdentifier("db_update_" + String.valueOf(i),
                        "raw", mContext.getPackageName());

                stream = mContext.getResources().openRawResource(resID);
                reader = new InputStreamReader(stream);
                bufferedReader = new BufferedReader(reader);

                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    line = line.trim();

                    // Don't bother processing blank lines
                    if(line.length() > 0)
                    {
                        mDb.execSQL(line);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Failed to update database.");
        }
        finally
        {
            try
            {
                if (stream != null)
                {
                    stream.close();
                }
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException ex)
            {
                Log.e(TAG, "Failed to close streams.");
            }
        }
    }
}
