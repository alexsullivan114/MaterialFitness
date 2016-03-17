package peoples.materialfitness.Model.Exercise;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.JsonDeserializer;
import peoples.materialfitness.Model.MuscleGroup.MuscleGroup;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by Alex Sullivan on 3/17/16.
 */
public class ExerciseJsonDeserializer implements JsonDeserializer
{
    @Override
    public Exercise deserialize(JsonObject jsonObject)
    {
        String title = jsonObject.get("title").getAsString();
        String muscleGroupString = jsonObject.get("muscleGroup").getAsString();
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromTitle(muscleGroupString, MaterialFitnessApplication.getApplication());
        Exercise exercise = new Exercise();
        exercise.setMuscleGroup(muscleGroup);
        exercise.setTitle(title);
        return exercise;
    }

    @Override
    public List<Exercise> deserialize(JsonArray jsonArray)
    {
        List<Exercise> returnList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray)
        {
            JsonObject exerciseJson = jsonElement.getAsJsonObject();
            Exercise exercise = deserialize(exerciseJson);
            returnList.add(exercise);
        }

        return returnList;
    }
}
