package peoples.materialfitness.Database;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
@Parcel(analyze = RepWeightMapping.class)
public class RepWeightMapping extends SugarRecord<RepWeightMapping>
{
    public static final String EXERCISE_SESSION_ID_COLUMN = StringUtil.toSQLName("exerciseSessionId");

    int weight;
    int numReps;
    long exerciseSessionId;

    public RepWeightMapping()
    {
    }

    public RepWeightMapping(int weight, int numReps, long exerciseSessionId)
    {
        this.weight = weight;
        this.numReps = numReps;
        this.exerciseSessionId = exerciseSessionId;
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
