package peoples.materialfitness.Core;

import android.app.Application;

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
    }

    public static MaterialFitnessApplication getApplication()
    {
        return application;
    }
}
