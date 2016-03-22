package peoples.materialfitness.Util;

import android.content.Context;
import android.content.SharedPreferences;

import peoples.materialfitness.Model.WeightUnit;


/**
 * Created by Alex Sullivan on 10/20/2015.
 *
 * Class to handle preference management. This is really a container class for the SharedPreferences
 * object so that individual views don't need to worry about the verbosity that goes along with
 * preferences in Android. I'd like to clean it up in such a way that it doesn't require a giant
 * long list of static final Strings...but I'm not sure if that's really possible.
 */
public class PreferenceManager
{
    private static final String prefFileName = "MaterialFitnessPreferences";
    private static PreferenceManager instance;
    private Context context;
    private SharedPreferences prefs;

    private static final String WEIGHT_UNIT_KEY = "weightUnitKey";

    private PreferenceManager(Context context)
    {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
    public static PreferenceManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    public void setUnits(WeightUnit weightUnit)
    {
        prefs.edit().putString(WEIGHT_UNIT_KEY, weightUnit.descriptor).apply();
    }

    public WeightUnit getUnits()
    {
        String weightUnitsDescriptor = prefs.getString(WEIGHT_UNIT_KEY, WeightUnit.IMPERIAL.descriptor);
        return WeightUnit.fromDesciptor(weightUnitsDescriptor);
    }

}
