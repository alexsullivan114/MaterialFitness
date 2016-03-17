package peoples.materialfitness.Model.WeightSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Model.JsonDeserializer;

/**
 * Created by Alex Sullivan on 3/17/16.
 */
public class WeightSetJsonDeserializer implements JsonDeserializer
{
    @Override
    public WeightSet deserialize(JsonObject jsonObject)
    {
        int weight = jsonObject.get("weight").getAsInt();
        int reps = jsonObject.get("reps").getAsInt();
        WeightSet weightSet = new WeightSet();
        weightSet.setWeight(weight);
        weightSet.setNumReps(reps);
        return weightSet;
    }

    @Override
    public List<WeightSet> deserialize(JsonArray jsonArray)
    {
        List<WeightSet> returnList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray)
        {
            JsonObject weightSetObject = jsonElement.getAsJsonObject();
            WeightSet weightSet = deserialize(weightSetObject);
            returnList.add(weightSet);
        }

        return returnList;
    }
}
