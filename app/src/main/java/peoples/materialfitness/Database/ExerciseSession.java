package peoples.materialfitness.Database;

import android.support.v4.util.SimpleArrayMap;
import android.util.ArrayMap;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex Sullivan on 10/20/2015.
 */
public class ExerciseSession extends SugarRecord<ExerciseSession>
{
    // The exercise associated with this session
    private Exercise exercise;
    // A mapping of reps to weights
    private HashMap<Integer, Integer> repWeightMap = new HashMap<>();
    // Required empty constructor for Sugar Record.
    public ExerciseSession(){}

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
