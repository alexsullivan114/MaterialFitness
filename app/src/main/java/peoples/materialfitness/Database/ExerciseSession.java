package peoples.materialfitness.Database;


import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Sullivan on 10/20/2015.
 *
 * An exercise session. This represents a certain number of sets at a certain weight for a certain
 * {@link peoples.materialfitness.Database.Exercise} object. For example, if I were to do
 * 5 sets of squats at 10lbs, then 20lbs, then 30lbs, then 40lbs, then a giant
 * PR at 320lbs, then this would be a {@link peoples.materialfitness.Database.ExerciseSession}
 */
@Parcel(value = Parcel.Serialization.BEAN, analyze = ExerciseSession.class)
public class ExerciseSession
{
    long id;
    // The exercise associated with this session
    Exercise exercise;
    // A mapping of sets to weights. We'll populate this ourselves.
    // the ID of the parent workout session
    long workoutSessionId;

    List<WeightSet> sets = new ArrayList<>();

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

    public List<WeightSet> getSets()
    {
        return sets;
    }

    public void setSets(List<WeightSet> sets)
    {
        this.sets = sets;
    }

    public void addSet(WeightSet set)
    {
        this.sets.add(set);
        set.setExerciseSessionId(this.getId());
    }

    public long getWorkoutSessionId()
    {
        return workoutSessionId;
    }

    public void setWorkoutSessionId(long workoutSessionId)
    {
        this.workoutSessionId = workoutSessionId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    @Override
    public String toString()
    {
        return exercise.getTitle() + " Session";
    }
}
