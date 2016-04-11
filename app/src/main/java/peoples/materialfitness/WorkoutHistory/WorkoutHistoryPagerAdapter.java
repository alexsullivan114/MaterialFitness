package peoples.materialfitness.WorkoutHistory;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.google.common.base.Optional;

import java.util.List;

import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragment;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

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
        return LogWorkoutFragment.newInstance(Optional.of(workoutSession));
    }

    @Override
    public int getCount()
    {
        return sortedWorkoutSessions.size();
    }
}
