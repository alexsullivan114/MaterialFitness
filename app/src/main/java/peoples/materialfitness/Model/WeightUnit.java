package peoples.materialfitness.Model;

import android.content.res.Resources;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 3/22/16.
 */
public enum WeightUnit
{
    IMPERIAL(MaterialFitnessApplication.getApplication().getResources().getString(R.string.lbs)),
    METRIC(MaterialFitnessApplication.getApplication().getResources().getString(R.string.kgs));

    private final String descriptor;

    WeightUnit(String descriptor)
    {
        this.descriptor = descriptor;
    }

    public static WeightUnit fromDesciptor(String descriptor)
    {
        Resources resources = MaterialFitnessApplication.getApplication().getResources();

        if (descriptor.equals(resources.getString(R.string.lbs)))
        {
            return IMPERIAL;
        }
        else
        {
            return METRIC;
        }
    }

    public String getUnitString()
    {
        return descriptor;
    }
}
