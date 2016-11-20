package peoples.materialfitness.Navigation;

import android.view.MenuItem;

import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragment;
import peoples.materialfitness.R;
import peoples.materialfitness.Schedule.ScheduleFragment;
import peoples.materialfitness.View.BaseFragment;
import peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryPager.WorkoutHistoryPagerFragment;

/**
 * Created by Alex Sullivan on 11/27/15.
 */
public enum NavigationItem
{
    // ALS 12/25/2015: MURRY CHRISTMAS! Also just refactored this - if I understand enums correctly,
    // which I almost certainly don't, these guys will be created on app start, and they're static
    // and final, so I thiiiiink these guys will be in memory 5ever. Bad for memory management, and
    // app start time, yes.
    // Good for speed switching back and forth? YOU KNOW IT!
    // Maybe revisit this later.

    NAV_ITEM_LOG_WORKOUT(0),
    NAV_ITEM_WORKOUT_HISTORY(1),
    NAV_ITEM_SCHEDULE(2);

    private final int position;

    NavigationItem(int position)
    {
        this.position = position;
    }

    public BaseFragment getFragmentForNavItem()
    {
        switch (this)
        {
            case NAV_ITEM_LOG_WORKOUT: return LogWorkoutFragment.newInstance();
            case NAV_ITEM_WORKOUT_HISTORY: return WorkoutHistoryPagerFragment.newInstance();
            case NAV_ITEM_SCHEDULE: return ScheduleFragment.newInstance();
            default: throw new RuntimeException("Failed to provide a fragment for nav item " + this);
        }
    }

    public int getPosition()
    {
        return this.position;
    }

    public static NavigationItem getNavItemFromMenuItem(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.log_workout:
                return NAV_ITEM_LOG_WORKOUT;
            case R.id.workout_history:
                return NAV_ITEM_WORKOUT_HISTORY;
            case R.id.workout_schedule:
                return NAV_ITEM_SCHEDULE;
            default:
                return NAV_ITEM_LOG_WORKOUT;
        }
    }
}
