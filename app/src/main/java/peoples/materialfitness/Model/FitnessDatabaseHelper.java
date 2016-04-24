package peoples.materialfitness.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import peoples.materialfitness.Model.Fitnotes.FitnotesDeserializer;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionJsonDeserializer;
import peoples.materialfitness.R;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alex Sullivan on 2/28/16.
 */
public class FitnessDatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = FitnessDatabaseHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MaterialFitness.db";
    private static FitnessDatabaseHelper INSTANCE;
    // Flags for debugging/building DBs.
    public static boolean buildDebugDatabase = false;

    private Context mContext;
    private SQLiteDatabase mDb;

    public static synchronized FitnessDatabaseHelper getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new FitnessDatabaseHelper(context.getApplicationContext());
        }

        return INSTANCE;
    }

    private FitnessDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
    }

    /**
     * Grab the database connection being used by the database manager.
     *
     * @return The opened writeable database.
     */
    public SQLiteDatabase getDatabase()
    {
        if (mDb == null)
        {
            mDb = getWritableDatabase();
        }

        return mDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        mDb = db;
        applyUpdates();
        if (buildDebugDatabase)
        {
            buildDebugDatabase();
            buildDebugDatabase = false;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    private void buildDebugDatabase()
    {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.test_workout_database);
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        JsonArray workoutSessionsArray = (JsonArray) new JsonParser().parse(jsonReader);
        List<WorkoutSession> workoutSessions = new WorkoutSessionJsonDeserializer().deserialize(workoutSessionsArray);

        WorkoutSessionDatabaseInteractor interactor = new WorkoutSessionDatabaseInteractor();
        for (WorkoutSession workoutSession : workoutSessions)
        {
            interactor.cascadeSave(workoutSession).subscribe();
        }
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
                    if (line.length() > 0)
                    {
                        mDb.execSQL(line);
                    }
                }
            }
        } catch (Exception e)
        {
            Log.e(TAG, "Failed to update database.");
        } finally
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
            } catch (IOException ex)
            {
                Log.e(TAG, "Failed to close streams.");
            }
        }
    }
}
