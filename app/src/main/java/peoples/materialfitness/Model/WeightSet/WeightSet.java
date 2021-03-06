package peoples.materialfitness.Model.WeightSet;


import android.content.ContentValues;

import org.parceler.Parcel;

import peoples.materialfitness.Model.WeightUnits.WeightUnitConverter;

/**
 * Created by Alex Sullivan on 2/15/16.
 *
 * TODO Fuck this parcelor shit. I barely use it.
 */
@Parcel(value = Parcel.Serialization.BEAN)
public class WeightSet
{
    long id = -1;
    double weight;
    int numReps;
    long exerciseSessionId;

    public WeightSet()
    {
    }

    public WeightSet(double weight, int numReps)
    {
        this.weight = weight;
        this.numReps = numReps;
    }

    public WeightSet(final WeightSet oldSet)
    {
        this.id = oldSet.id;
        this.numReps = oldSet.numReps;
        this.weight = oldSet.weight;
        this.exerciseSessionId = oldSet.exerciseSessionId;
    }

    public long getExerciseSessionId()
    {
        return exerciseSessionId;
    }

    public void setExerciseSessionId(long exerciseSessionId)
    {
        this.exerciseSessionId = exerciseSessionId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    /**
     * Weights should ALWAYS be saved in metric, so it's assumed this weight is in KG.
     * @return Weight in KG of this rep
     */
    public double getWeight()
    {
        return weight;
    }

    /**
     * Returns the weight in the units the user has selected.
     * @return The weight in KG or LBS
     */
    public double getUserUnitsWeight()
    {
        return WeightUnitConverter.getDisplayWeight(weight);
    }

    public void setUserInputWeight(double userInputWeight)
    {
        this.weight = WeightUnitConverter.getMetricWeightFromUserInputWeight(userInputWeight);
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public int getNumReps()
    {
        return numReps;
    }

    public void setNumReps(int numReps)
    {
        this.numReps = numReps;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WeightSetContract._ID, id);
        contentValues.put(WeightSetContract.COLUMN_NAME_WEIGHT, weight);
        contentValues.put(WeightSetContract.COLUMN_NAME_REPS, numReps);
        contentValues.put(WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID, exerciseSessionId);

        return contentValues;
    }

    public static WeightSet getWeightSet(ContentValues contentValues)
    {
        WeightSet weightSet = new WeightSet();

        weightSet.setExerciseSessionId(contentValues.getAsLong(WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID));
        weightSet.setId(contentValues.getAsLong(WeightSetContract._ID));
        weightSet.setNumReps(contentValues.getAsInteger(WeightSetContract.COLUMN_NAME_REPS));
        weightSet.setWeight(contentValues.getAsDouble(WeightSetContract.COLUMN_NAME_WEIGHT));

        return weightSet;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        WeightSet weightSet = (WeightSet) o;

        return id == weightSet.id && weight == weightSet.weight &&
                numReps == weightSet.numReps &&
                exerciseSessionId == weightSet.exerciseSessionId;

    }

    @Override
    public int hashCode()
    {
        double result = (int) (id ^ (id >>> 32));
        result = 31 * result + weight;
        result = 31 * result + numReps;
        result = 31 * result + (int) (exerciseSessionId ^ (exerciseSessionId >>> 32));
        return (int)result;
    }

    @Override
    public String toString()
    {
        return "WeightSet{" +
                "exerciseSessionId=" + exerciseSessionId +
                ", weight=" + weight +
                ", numReps=" + numReps +
                '}';
    }
}
