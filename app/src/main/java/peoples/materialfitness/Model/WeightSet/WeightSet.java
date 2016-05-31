package peoples.materialfitness.Model.WeightSet;


import android.content.ContentValues;

import org.parceler.Parcel;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
@Parcel(value = Parcel.Serialization.BEAN)
public class WeightSet
{
    long id = -1;
    int weight;
    int numReps;
    boolean isPr = false;
    long exerciseSessionId;

    public WeightSet()
    {
    }

    public WeightSet(int weight, int numReps)
    {
        this.weight = weight;
        this.numReps = numReps;
    }

    public long getExerciseSessionId()
    {
        return exerciseSessionId;
    }

    public void setExerciseSessionId(long exerciseSessionId)
    {
        this.exerciseSessionId = exerciseSessionId;
    }

    public void setIsPr(boolean isPr)
    {
        this.isPr = isPr;
    }

    public boolean getIsPr()
    {
        return isPr;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
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
        contentValues.put(WeightSetContract.COLUMN_IS_PR, isPr);

        return contentValues;
    }

    public static WeightSet getWeightSet(ContentValues contentValues)
    {
        WeightSet weightSet = new WeightSet();

        weightSet.setExerciseSessionId(contentValues.getAsLong(WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID));
        weightSet.setId(contentValues.getAsLong(WeightSetContract._ID));
        weightSet.setNumReps(contentValues.getAsInteger(WeightSetContract.COLUMN_NAME_REPS));
        weightSet.setWeight(contentValues.getAsInteger(WeightSetContract.COLUMN_NAME_WEIGHT));
        weightSet.setIsPr(contentValues.getAsInteger(WeightSetContract.COLUMN_IS_PR) != 0);

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

        if (id != weightSet.id)
        {
            return false;
        }
        if (weight != weightSet.weight)
        {
            return false;
        }
        if (numReps != weightSet.numReps)
        {
            return false;
        }
        return exerciseSessionId == weightSet.exerciseSessionId;

    }

    @Override
    public int hashCode()
    {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + weight;
        result = 31 * result + numReps;
        result = 31 * result + (int) (exerciseSessionId ^ (exerciseSessionId >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "WeightSet{" +
                "exerciseSessionId=" + exerciseSessionId +
                ", weight=" + weight +
                ", numReps=" + numReps +
                ", isPr=" + isPr +
                '}';
    }
}
