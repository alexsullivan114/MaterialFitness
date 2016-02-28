package peoples.materialfitness.Database;


import org.parceler.Parcel;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
@Parcel(value = Parcel.Serialization.BEAN)
public class WeightSet
{
    long id;
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
}
