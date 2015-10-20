package peoples.materialfitness.Util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Alex Sullivan on 10/20/2015.
 */
public class PreferenceManager
{
    private static final String prefFileName = "MaterialFitnessPreferences";
    private static PreferenceManager instance;
    private Context context;
    private SharedPreferences prefs;

    private PreferenceManager(Context context)
    {
        this.context = context;
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
}
