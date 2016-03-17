package peoples.materialfitness.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by Alex Sullivan on 3/17/16.
 */
public interface JsonDeserializer<T>
{
    T deserialize(JsonObject jsonObject);
    List<T> deserialize(JsonArray jsonArray);
}
