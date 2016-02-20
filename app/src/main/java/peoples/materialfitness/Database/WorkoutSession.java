package peoples.materialfitness.Database;

import android.support.v4.util.SimpleArrayMap;

import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alex Sullivan on 10/4/2015.
 *
 * {@link WorkoutSession}'s represent a list of {@link ExerciseSession}'s. This is what really
 * describes a days workout - sets of squats and deadlifts and bench presses and whatnot all come
 * together to make up a {@link WorkoutSession}.
 *
 * Also I think I'm going a little crazy with the links...but I like them!
 *
 *  * Note: We use Serialization.BEAN because we want the ID of the sugar record object, but we
 * only want to analyze this class for parceling since there's other stuff we dont want.
 */
@Parcel(value = Parcel.Serialization.BEAN, analyze = WorkoutSession.class)
public class WorkoutSession extends SugarRecord<WorkoutSession>
{
    public static final String EXERCISES_COLUMN = StringUtil.toSQLName("exercises");
    public static final String DATE_COLUMN = StringUtil.toSQLName("workoutSessionDate");

    @Ignore
    List<ExerciseSession> exercises = new ArrayList<>();
    // millis since epoch
    long workoutSessionDate;
    public WorkoutSession()
    {
        // required empty constructor
    }

    public WorkoutSession(long sessionDate)
    {
        this.workoutSessionDate = sessionDate;
    }

    public void addExerciseSession(ExerciseSession session)
    {
        exercises.add(session);
    }

    public void addAllExerciseSessions(Collection<ExerciseSession> sessions)
    {
        exercises.addAll(sessions);
    }

    public boolean containsExercise(Exercise exercise)
    {
        for (ExerciseSession session: exercises)
        {
            if (session.getExercise().getTitle().equalsIgnoreCase(exercise.getTitle()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an exercise session to this workout iff it does not already contain that exercise.
     * @param exercise
     * @return True if the exercise was added, false otherwise.
     */
    public boolean uniqueAddExerciseSession(ExerciseSession exercise)
    {
        if (!containsExercise(exercise.getExercise()))
        {
            addExerciseSession(exercise);
            return true;
        }

        return false;
    }

    /**
     * Adds the exercise session, overwriting the current one if it exists.
     * @param exerciseSession Exercise session to replace the potentially existing one.
     */
    public void setExerciseSession(ExerciseSession exerciseSession)
    {
        for (int i = 0; i < exercises.size(); i++)
        {
            ExerciseSession existingSession = exercises.get(i);

            if (existingSession.getExercise().getTitle().equalsIgnoreCase(exerciseSession.getExercise().getTitle()))
            {
                exercises.set(i, exerciseSession);
                return;
            }
        }

        // If we got this far we didn't replace anything. Add it normally.
        addExerciseSession(exerciseSession);
    }

    public List<ExerciseSession> getExercises()
    {
        return exercises;
    }

    public void setExercises(List<ExerciseSession> exercises)
    {
        this.exercises = exercises;
    }

    public long getWorkoutSessionDate()
    {
        return workoutSessionDate;
    }

    public void setWorkoutSessionDate(long workoutSessionDate)
    {
        this.workoutSessionDate = workoutSessionDate;
    }

    @Override
    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public Long getId()
    {
        return this.id;
    }

    /**
     * Generate a map of muscle group to exercise sessions for this workout session.
     * @return A simple array mapping of muscle groups to exercise sessions.
     */
    public SimpleArrayMap<MuscleGroup, ArrayList<ExerciseSession>> getExerciseMap()
    {
        SimpleArrayMap<MuscleGroup, ArrayList<ExerciseSession>> map = new SimpleArrayMap<>();

        for (ExerciseSession session: exercises)
        {
            Exercise exercise = session.getExercise();
            ArrayList<ExerciseSession> muscleGroupExercises = map.get(exercise.getMuscleGroup());

            if (muscleGroupExercises == null)
            {
                muscleGroupExercises = new ArrayList<>();
            }

            muscleGroupExercises.add(session);
            map.put(exercise.getMuscleGroup(),muscleGroupExercises);
        }

        return map;
    }
}
