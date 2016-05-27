package peoples.materialfitness.Model;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 5/15/2016.
 */
public enum ScheduleDay
{
    MONDAY(-10, R.color.monday_schedule_color, R.color.monday_schedule_color_pressed, R.string.monday),
    TUESDAY(-11, R.color.tuesday_schedule_color, R.color.tuesday_schedule_color_pressed, R.string.tuesday),
    WEDNESDAY(-12, R.color.wednesday_schedule_color, R.color.wednesday_schedule_color_pressed, R.string.wednesday),
    THURSDAY(-13, R.color.thursday_schedule_color, R.color.thursday_schedule_color_pressed, R.string.thursday),
    FRIDAY(-14, R.color.friday_schedule_color, R.color.friday_schedule_color_pressed, R.string.friday),
    SATURDAY(-15, R.color.saturday_schedule_color, R.color.saturday_schedule_color_pressed, R.string.saturday),
    SUNDAY(-16, R.color.sunday_schedule_color, R.color.sunday_schedule_color_pressed, R.string.sunday);

    private final long workoutSessionId;
    private final @ColorRes int colorRes;
    private final @ColorRes int pressedColorRes;
    private final @StringRes int displayName;

    ScheduleDay(long workoutSessionId, @ColorRes int colorRes, @ColorRes int pressedColorRes, @StringRes int displayName)
    {
        this.workoutSessionId = workoutSessionId;
        this.colorRes = colorRes;
        this.displayName = displayName;
        this.pressedColorRes = pressedColorRes;
    }

    public long getWorkoutSessionId()
    {
        return workoutSessionId;
    }

    public @ColorRes int getColorResInt()
    {
        return colorRes;
    }

    public @StringRes int getDisplayName()
    {
        return displayName;
    }

    public @ColorRes int getPressedColorRes()
    {
        return pressedColorRes;
    }
}
