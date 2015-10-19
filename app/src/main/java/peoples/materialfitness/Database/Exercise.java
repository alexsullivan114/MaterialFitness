package peoples.materialfitness.Database;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.UUID;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public class Exercise extends SugarRecord<Exercise>
{
    private String title;
    private MuscleGroup muscleGroup;

    public static final String TITLE_COLUMN = StringUtil.toSQLName("title");
    public static final String MUSCLE_GROUP_COLUMN = StringUtil.toSQLName("muscleGroup");

    /**
     * Empty constructor required for sugar record OEM.
     */
    public Exercise()
    {

    }

    public Exercise(String title, MuscleGroup muscleGroup)
    {
        this.title = title;
        this.muscleGroup = muscleGroup;
    }

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
}
