package peoples.materialfitness.WorkoutDetails;

import java.util.List;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public interface WorkoutDetailsActivityInterface extends BaseActivityInterface
{
    void setTitle(String title);
    void setChartData(List<WorkoutSession> workoutSessionList, Exercise exercise);
}
