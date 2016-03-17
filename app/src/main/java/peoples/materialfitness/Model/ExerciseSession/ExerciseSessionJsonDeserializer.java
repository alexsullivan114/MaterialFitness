package peoples.materialfitness.Model.ExerciseSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseJsonDeserializer;
import peoples.materialfitness.Model.JsonDeserializer;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetJsonDeserializer;

/**
 * Created by Alex Sullivan on 3/17/16.
 */
public class ExerciseSessionJsonDeserializer implements JsonDeserializer
{
    @Override
    public ExerciseSession deserialize(JsonObject jsonObject)
    {
        JsonObject exerciseObject = jsonObject.getAsJsonObject("exercise");
        Exercise exercise = new ExerciseJsonDeserializer().deserialize(exerciseObject);
        JsonArray weightSetsJsonArray = jsonObject.getAsJsonArray("weightSets");
        List<WeightSet> weightSetList = new WeightSetJsonDeserializer().deserialize(weightSetsJsonArray);
        ExerciseSession exerciseSession = new ExerciseSession();
        exerciseSession.setExercise(exercise);
        exerciseSession.setSets(weightSetList);
        return exerciseSession;
    }

    @Override
    public List<ExerciseSession> deserialize(JsonArray jsonArray)
    {
        List<ExerciseSession> returnList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray)
        {
            JsonObject exerciseSessionJson = jsonElement.getAsJsonObject();
            ExerciseSession exerciseSession = deserialize(exerciseSessionJson);
            returnList.add(exerciseSession);
        }

        return returnList;
    }
}
