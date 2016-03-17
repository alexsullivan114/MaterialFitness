package peoples.materialfitness.Core;

import android.app.Application;

import peoples.materialfitness.BuildConfig;
import peoples.materialfitness.Model.FitnessDatabaseHelper;

import com.facebook.stetho.Stetho;

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
    }

    public static MaterialFitnessApplication getApplication()
    {
        return application;
    }
}
