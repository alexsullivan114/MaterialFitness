package peoples.materialfitness.WorkoutHistory;

import java.util.List;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 12/27/15.
 */
public interface WorkoutHistoryFragmentInterface extends BaseFragmentInterface
{
    void setWorkoutSessions(List<WorkoutSession> workoutSessions);
}
