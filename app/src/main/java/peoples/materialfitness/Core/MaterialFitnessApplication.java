package peoples.materialfitness.Core;

import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;

import peoples.materialfitness.BuildConfig;
import peoples.materialfitness.FitnotesImport.FitnotesImporterActivity;
import peoples.materialfitness.Model.FitnessDatabaseHelper;
import peoples.materialfitness.Util.Constants;

/**
 * Created by Alex Sullivan on 2/28/16.
 */
public class MaterialFitnessApplication extends Application
{

    private static MaterialFitnessApplication application;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initApplication();
        initThirdPartySdks();
        initDebugDatabases();
    }

    private void initApplication()
    {
        application = this;
    }

    private void initThirdPartySdks()
    {
        // TODO: Get debug builds working and wrap this in one of dem.
        Stetho.initializeWithDefaults(this);
    }

    private void initDebugDatabases()
    {
        if (BuildConfig.BUILD_TYPE.equals(Constants.DEBUG_DB_BUILD_TYPE))
        {
            FitnessDatabaseHelper.buildDebugDatabase = true;
        }
    }

    public static MaterialFitnessApplication getApplication()
    {
        return application;
    }
}
