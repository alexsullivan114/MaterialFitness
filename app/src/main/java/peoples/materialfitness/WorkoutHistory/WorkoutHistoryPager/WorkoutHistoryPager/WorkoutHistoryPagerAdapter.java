package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryPager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistory.WorkoutHistoryFragment;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class WorkoutHistoryPagerAdapter extends FragmentStatePagerAdapter
{
    private final List<WorkoutSession> sortedWorkoutSessions;

    public WorkoutHistoryPagerAdapter(@NonNull FragmentManager fm,
                                      @NonNull  List<WorkoutSession> sortedWorkoutSessions)
    {
        super(fm);
        this.sortedWorkoutSessions = sortedWorkoutSessions;
    }

    @Override
    public Fragment getItem(int position)
    {
        WorkoutSession workoutSession = sortedWorkoutSessions.get(position);
        WorkoutHistoryFragment fragment = WorkoutHistoryFragment.newInstance(workoutSession);
        fragment.setPresenterKey(fragment.TAG + String.valueOf(position));
        return fragment;
    }

    @Override
    public int getCount()
    {
        return sortedWorkoutSessions.size();
    }
}
