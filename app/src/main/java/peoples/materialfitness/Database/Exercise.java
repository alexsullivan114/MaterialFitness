package peoples.materialfitness.Database;

import com.orm.SugarRecord;

import java.util.UUID;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public class Exercise extends SugarRecord<Exercise>
{
    private String title;
    private MuscleGroup muscleGroup;
    private UUID uuid;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public MuscleGroup getMuscleGroup()
    {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup)
    {
        this.muscleGroup = muscleGroup;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }
}
