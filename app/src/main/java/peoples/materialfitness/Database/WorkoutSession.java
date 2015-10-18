package peoples.materialfitness.Database;

import android.support.v4.util.SimpleArrayMap;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public class WorkoutSession extends SugarRecord<WorkoutSession>
{
    private List<Exercise> exercises = new ArrayList<>();
    // millis since epoch
    private long workoutSessionDate;

    WorkoutSession(long sessionDate)
    {
        this.workoutSessionDate = sessionDate;
    }

    public List<Exercise> addExercise(Exercise exercise)
    {
        exercises.add(exercise);
        return exercises;
    }

    public List<Exercise> addAllExercises(Collection<Exercise> exerciseCollection)
    {
        exercises.addAll(exerciseCollection);
        return exercises;
    }


    public List<Exercise> getExercises()
    {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises)
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
     * Generate a map of muscle group to exercises for this workout session.
     * @return A simple array mapping of muscle groups to exercises.
     */
    public SimpleArrayMap<MuscleGroup, ArrayList<Exercise>> getExerciseMap()
    {
        SimpleArrayMap<MuscleGroup, ArrayList<Exercise>> map = new SimpleArrayMap<>();

        for (Exercise exercise: exercises)
        {
            ArrayList<Exercise> muscleGroupExercises = map.get(exercise.getMuscleGroup());

            if (muscleGroupExercises == null)
            {
                muscleGroupExercises = new ArrayList<Exercise>();
            }

            muscleGroupExercises.add(exercise);
            map.put(exercise.getMuscleGroup(),muscleGroupExercises);
        }

        return map;
    }
}
