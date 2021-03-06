package peoples.materialfitness.Model.WeightUnits;

import peoples.materialfitness.Util.PreferenceManager;

/**
 * Created by Alex Sullivan on 7/30/16.
 *
 * Class to handle converting weight units.
 */
public class WeightUnitConverter
{
    private static final double LB_PER_KG = 2.20462;
    private static final double KG_PER_LB = 0.453592;

    public static double getDisplayWeight(double kgWeight)
    {
        if (PreferenceManager.getInstance().getUnits() == WeightUnit.IMPERIAL)
        {
            return kgWeight * LB_PER_KG;
        }
        else
        {
            return kgWeight;
        }
    }

    public static double getImperialWeight(double kgWeight)
    {
        return kgWeight * LB_PER_KG;
    }

    public static double getMetricWeight(double lbWeight)
    {
        return lbWeight * KG_PER_LB;
    }

    public static double getMetricWeight(double weightValue, WeightUnit weightUnit)
    {
        if (weightUnit == WeightUnit.IMPERIAL)
        {
            return weightValue * KG_PER_LB;
        }
        else
        {
            return weightValue;
        }
    }

    public static double getMetricWeightFromUserInputWeight(double weightValue)
    {
        WeightUnit userWeightUnit = PreferenceManager.getInstance().getUnits();
        return getMetricWeight(weightValue, userWeightUnit);
    }
}
