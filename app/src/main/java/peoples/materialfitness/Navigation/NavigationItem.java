package peoples.materialfitness.Navigation;

import android.app.Fragment;
import android.view.MenuItem;

import peoples.materialfitness.R;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragment;

/**
 * Created by Alex Sullivan on 11/27/15.
 */
public enum NavigationItem
{
    NAV_ITEM_LOG_WORKOUT,
    NAV_ITEM_WORKOUT_HISTORY;

    public static NavigationItem getNavItemFromMenuItem(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.log_workout: return NAV_ITEM_LOG_WORKOUT;
            case R.id.workout_history: return NAV_ITEM_WORKOUT_HISTORY;
            default: return NAV_ITEM_LOG_WORKOUT;
        }
    }

    public Fragment getFragmentForNavItem()
    {
        switch (this)
        {
            case NAV_ITEM_LOG_WORKOUT: return LogWorkoutFragment.newInstance();
            case NAV_ITEM_WORKOUT_HISTORY:
            default: return LogWorkoutFragment.newInstance();
        }
    }
}
