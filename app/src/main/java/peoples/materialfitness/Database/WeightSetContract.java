package peoples.materialfitness.Database;

import android.provider.BaseColumns;

/**
 * Created by Alex Sullivan on 2/28/16.
 */
public class WeightSetContract implements BaseColumns
{
    public static final String TABLE_NAME = "WeightSet";
    public static final String COLUMN_NAME_WEIGHT = "weight";
    public static final String COLUMN_NAME_REPS = "reps";
    public static final String COLUMN_NAME_EXERCISE_SESSION_ID = "exerciseSessionId";
}
