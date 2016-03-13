package peoples.materialfitness.Database;


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

        return contentValues;
    }

    public static WeightSet getWeightSet(ContentValues contentValues)
    {
        WeightSet weightSet = new WeightSet();

        weightSet.setExerciseSessionId(contentValues.getAsLong(WeightSetContract.COLUMN_NAME_EXERCISE_SESSION_ID));
        weightSet.setId(contentValues.getAsLong(WeightSetContract._ID));
        weightSet.setNumReps(contentValues.getAsInteger(WeightSetContract.COLUMN_NAME_REPS));
        weightSet.setWeight(contentValues.getAsInteger(WeightSetContract.COLUMN_NAME_WEIGHT));

        return weightSet;
    }
}
