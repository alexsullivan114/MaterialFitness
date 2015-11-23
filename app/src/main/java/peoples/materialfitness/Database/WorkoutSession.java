package peoples.materialfitness.Database;

import android.support.v4.util.SimpleArrayMap;

import com.orm.SugarRecord;

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
 */
public class WorkoutSession extends SugarRecord<WorkoutSession>
{
    private List<ExerciseSession> exercises = new ArrayList<>();
    // millis since epoch
    private long workoutSessionDate;

    WorkoutSession(long sessionDate)
    {
        this.workoutSessionDate = sessionDate;
    }

    public List<ExerciseSession> addExercise(ExerciseSession session)
    {
        exercises.add(session);
        return exercises;
    }

    public List<ExerciseSession> addAllExercises(Collection<ExerciseSession> sessions)
    {
        exercises.addAll(sessions);
        return exercises;
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
