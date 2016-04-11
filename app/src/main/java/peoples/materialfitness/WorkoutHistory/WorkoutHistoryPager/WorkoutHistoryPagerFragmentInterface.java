package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager;

import java.util.List;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 12/27/15.
 */
public interface WorkoutHistoryPagerFragmentInterface extends BaseFragmentInterface
{
    void setWorkoutSessions(List<WorkoutSession> workoutSessions);
    void setTitle(String title);
}
