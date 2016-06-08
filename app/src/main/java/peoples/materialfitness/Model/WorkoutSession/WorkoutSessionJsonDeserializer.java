package peoples.materialfitness.Model.WorkoutSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionJsonDeserializer;
import peoples.materialfitness.Model.JsonDeserializer;

/**
 * Created by Alex Sullivan on 3/17/16.
 */
public class WorkoutSessionJsonDeserializer implements JsonDeserializer<WorkoutSession>
{
    @Override
    public WorkoutSession deserialize(JsonObject jsonObject)
    {
        long date = jsonObject.get("workoutSessionDate").getAsLong();
        JsonArray exerciseSessionsJson = jsonObject.get("exerciseSessions").getAsJsonArray();
        List<ExerciseSession> exerciseSessions = new ExerciseSessionJsonDeserializer().deserialize(exerciseSessionsJson);

        WorkoutSession workoutSession = new WorkoutSession(date);
        workoutSession.setExerciseSessions(exerciseSessions);
        return workoutSession;
    }

    @Override
    public List<WorkoutSession> deserialize(JsonArray jsonArray)
    {
        List<WorkoutSession> returnList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray)
        {
            JsonObject workoutSessionJson = jsonElement.getAsJsonObject();
            WorkoutSession workoutSession = deserialize(workoutSessionJson);
            returnList.add(workoutSession);
        }

        return returnList;
    }
}
