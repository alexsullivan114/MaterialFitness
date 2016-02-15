package peoples.materialfitness.Database;

import android.support.v4.util.SimpleArrayMap;
import android.util.ArrayMap;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex Sullivan on 10/20/2015.
 *
 * An exercise session. This represents a certain number of reps at a certain weight for a certain
 * {@link peoples.materialfitness.Database.Exercise} object. For example, if I were to do
 * 5 reps of squats at 10lbs, then 20lbs, then 30lbs, then 40lbs, then a giant
 * PR at 320lbs, then this would be a {@link peoples.materialfitness.Database.ExerciseSession}
 */
@Parcel(Parcel.Serialization.BEAN)
public class ExerciseSession
{
    // The exercise associated with this session
    Exercise exercise;
    // A mapping of reps to weights
    HashMap<Integer, Integer> repWeightMap = new HashMap<>();
    // Required empty constructor for Sugar Record.
    public ExerciseSession(){}

    @ParcelConstructor
    public ExerciseSession(Exercise exercise)
    {
        this.exercise = exercise;
    }

    public Exercise getExercise()
    {
        return exercise;
    }

    public void setExercise(Exercise exercise)
    {
        this.exercise = exercise;
    }

    public HashMap<Integer, Integer> getRepWeightMap()
    {
        return repWeightMap;
    }

    public void setRepWeightMap(HashMap<Integer, Integer> repWeightMap)
    {
        this.repWeightMap = repWeightMap;
    }

    public HashMap<Integer, Integer> addRep(int weight)
    {
        Set<Integer> keys = repWeightMap.keySet();
        List<Integer> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);

        int lastRep = keyList.get(keyList.size());

        repWeightMap.put(lastRep, weight);
        return repWeightMap;
    }
}
