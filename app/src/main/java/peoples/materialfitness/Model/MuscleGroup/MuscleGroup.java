package peoples.materialfitness.Model.MuscleGroup;

import android.content.Context;

import java.util.List;

import peoples.materialfitness.R;
import rx.Observable;

/**
 * Created by Alex Sullivan on 10/4/2015.
 *
 * {@link MuscleGroup}'s are whatever muscle group is targeted by a workout. Pretty self explanatory.
 * Let's try to keep this enum reasonable, shall we? We all know Enums are "evil". Ergh.
 *
 *
 *
 *
 * #REVOLUTIONARY.
 */
public enum MuscleGroup
{
    SHOULDERS(R.string.shoulders, 0),
    CHEST(R.string.chest, 1),
    LEGS(R.string.legs, 2),
    ARMS(R.string.arms, 3),
    BACK(R.string.back, 4),
    CORE(R.string.core, 5);

    private int mTitleId;
    private int mValue;

    MuscleGroup(int titleId, int value)
    {
        this.mTitleId = titleId;
        this.mValue = value;
    }

    public int getTitleId()
    {
        return this.mTitleId;
    }

    public int getValue()
    {
        return mValue;
    }

    public String getTitle(Context context)
    {
        return context.getString(this.mTitleId);
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
            if (muscleGroup.getTitle(context).equalsIgnoreCase(title))
            {
                return muscleGroup;
            }
        }

        throw new RuntimeException("Tried to get MuscleGroup from unkown title: " + title);
    }

    public static Observable<List<String>> getMuscleGroupTitles(Context context)
    {
        return Observable.from(MuscleGroup.class.getEnumConstants())
                .map(muscleGroup -> muscleGroup.getTitle(context))
                .toList();
    }

    public static MuscleGroup muscleGroupFromValue(int value)
    {
        switch (value)
        {
            case 0: return MuscleGroup.SHOULDERS;
            case 1: return MuscleGroup.CHEST;
            case 2: return MuscleGroup.LEGS;
            case 3: return MuscleGroup.ARMS;
            case 4: return MuscleGroup.BACK;
            case 5: return MuscleGroup.CORE;
            // everyones always doin' arms.
            default: return MuscleGroup.ARMS;
        }
    }
}
