package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryCalendarDialog;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 4/16/2016.
 */
public interface WorkoutHistoryCalendarDialogFragmentInterface extends BaseFragmentInterface
{
    void postWorkoutSession(WorkoutSession workoutSession);
    void dismissCalendar();
}
