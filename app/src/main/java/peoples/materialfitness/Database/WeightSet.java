package peoples.materialfitness.Database;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
@Parcel(analyze = WeightSet.class)
public class WeightSet extends SugarRecord<WeightSet>
{
    public static final String EXERCISE_SESSION_ID_COLUMN = StringUtil.toSQLName("exerciseSessionId");

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
}
