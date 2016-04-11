package peoples.materialfitness.WorkoutHistory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class WorkoutHistoryPagerAdapter extends FragmentStatePagerAdapter
{
    private final List<WorkoutSession> sortedWorkoutSessions;

    public WorkoutHistoryPagerAdapter(FragmentManager fm, List<WorkoutSession> sortedWorkoutSessions)
    {
        super(fm);
        this.sortedWorkoutSessions = sortedWorkoutSessions;
    }

    @Override
    public Fragment getItem(int position)
    {
        return null;
    }

    @Override
    public int getCount()
    {
        return 0;
    }
}
