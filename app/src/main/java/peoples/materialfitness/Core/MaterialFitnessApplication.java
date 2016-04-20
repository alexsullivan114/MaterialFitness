package peoples.materialfitness.Core;

import android.app.Application;

import com.facebook.stetho.Stetho;

import peoples.materialfitness.BuildConfig;
import peoples.materialfitness.Model.FitnessDatabaseHelper;

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
        application = this;
        // TODO: Get debug builds working and wrap this in one of dem.
        Stetho.initializeWithDefaults(this);
        if (BuildConfig.BUILD_TYPE.equals("debugFreshDb"))
        {
            FitnessDatabaseHelper.buildDebugDatabase = true;
        }
        else if (BuildConfig.BUILD_TYPE.equals("importFitnotesDb"))
        {
            FitnessDatabaseHelper.importFitnotesDb = true;
        }
    }

    public static MaterialFitnessApplication getApplication()
    {
        return application;
    }
}
