package peoples.materialfitness.Navigation;

import android.view.MenuItem;

import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragment;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;
import peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryPager.WorkoutHistoryPagerFragment;

/**
 * Created by Alex Sullivan on 11/27/15.
 */
public enum NavigationItem
{
    // ALS 12/25/2015: MURRY CHRISTMAS!.

    NAV_ITEM_LOG_WORKOUT(0),
    NAV_ITEM_WORKOUT_HISTORY(1),
    NAV_ITEM_SETTINGS(2);

    private final int position;

    NavigationItem(int position)
    {
        this.position = position;
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
            case R.id.settings:
                return NAV_ITEM_SETTINGS;
            default:
                return NAV_ITEM_LOG_WORKOUT;
        }
    }
}
