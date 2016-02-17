package peoples.materialfitness.Database;

import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Sullivan on 10/20/2015.
 *
 * An exercise session. This represents a certain number of reps at a certain weight for a certain
 * {@link peoples.materialfitness.Database.Exercise} object. For example, if I were to do
 * 5 reps of squats at 10lbs, then 20lbs, then 30lbs, then 40lbs, then a giant
 * PR at 320lbs, then this would be a {@link peoples.materialfitness.Database.ExerciseSession}
 */
@Parcel(analyze = ExerciseSession.class)
public class ExerciseSession extends SugarRecord<ExerciseSession>
{
    public static final String EXERCISE_COLUMN = StringUtil.toSQLName("exercise");
    public static final String WORKOUT_SESSION_ID_COLUMN = StringUtil.toSQLName("workoutSessionId");

    // The exercise associated with this session
    Exercise exercise;
    // A mapping of reps to weights. We'll populate this ourselves.
    @Ignore
    List<RepWeightMapping> reps = new ArrayList<>();
    // the ID of the parent workout session
    long workoutSessionId;
    // Required empty constructor for Sugar Record.
    public ExerciseSession(){}

    public ExerciseSession(Exercise exercise, long workoutSessionId)
    {
        this.exercise = exercise;
        this.workoutSessionId = workoutSessionId;
    }

    public Exercise getExercise()
    {
        return exercise;
    }

    public void setExercise(Exercise exercise)
    {
        this.exercise = exercise;
    }

    public List<RepWeightMapping> getReps()
    {
        return reps;
    }

    public void setReps(List<RepWeightMapping> reps)
    {
        this.reps = reps;
    }

    public long getWorkoutSessionId()
    {
        return workoutSessionId;
    }

    public void setWorkoutSessionId(long workoutSessionId)
    {
        this.workoutSessionId = workoutSessionId;
    }

    @Override
    public String toString()
    {
        return exercise.getTitle() + " Session";
    }
}
