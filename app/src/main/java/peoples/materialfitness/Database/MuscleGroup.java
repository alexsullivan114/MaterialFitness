package peoples.materialfitness.Database;

import android.content.Context;

import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public enum MuscleGroup
{
    SHOULDERS(R.string.shoulders),
    CHEST(R.string.chest),
    LEGS(R.string.legs),
    ARMS(R.string.arms),
    BACK(R.string.back);

    private int titleId;

    MuscleGroup(int titleId)
    {
        this.titleId = titleId;
    }

    public int getTitleId()
    {
        return this.titleId;
    }

    public String getTitle(Context context)
    {
        return context.getString(this.titleId);
    }

    /**
     * Gets the given muscle group from the given string title.
     * @param title Title of the muscle group to retrieve
     * @param context Context used to get the title of the enum values of the muscle group
     * @return The matching MuscleGroup.
     */
    public static MuscleGroup muscleGroupFromTitle(String title, Context context)
    {
        for (MuscleGroup muscleGroup: MuscleGroup.class.getEnumConstants())
        {
            if (muscleGroup.getTitle(context).equals(title))
            {
                return muscleGroup;
            }
        }

        throw new RuntimeException("Tried to get MuscleGroup from unkown title: " + title);
    }
}
