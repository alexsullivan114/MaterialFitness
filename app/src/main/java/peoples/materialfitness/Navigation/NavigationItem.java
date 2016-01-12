package peoples.materialfitness.Navigation;

import android.view.MenuItem;

import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;
import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragment;
import peoples.materialfitness.WorkoutHistoryFragment.WorkoutHistoryFragment;

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

    NAV_ITEM_LOG_WORKOUT(LogWorkoutFragment.newInstance()),
    NAV_ITEM_WORKOUT_HISTORY(WorkoutHistoryFragment.newInstance());

    private BaseFragment fragment;

    NavigationItem(BaseFragment fragment)
    {
        this.fragment = fragment;
    }

    public BaseFragment getFragmentForNavItem()
    {
        return this.fragment;
    }

    public static NavigationItem getNavItemFromMenuItem(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.log_workout:
                return NAV_ITEM_LOG_WORKOUT;
            case R.id.workout_history:
                return NAV_ITEM_WORKOUT_HISTORY;
            default:
                return NAV_ITEM_LOG_WORKOUT;
        }
    }
}
