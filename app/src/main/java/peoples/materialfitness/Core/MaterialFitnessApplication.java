package peoples.materialfitness.Core;

import android.app.Application;

import com.facebook.stetho.BuildConfig;
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
    }

    public static MaterialFitnessApplication getApplication()
    {
        return application;
    }
}
