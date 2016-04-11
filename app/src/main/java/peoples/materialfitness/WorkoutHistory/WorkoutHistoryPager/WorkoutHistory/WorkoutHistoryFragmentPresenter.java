package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistory;

import com.google.common.base.Optional;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class WorkoutHistoryFragmentPresenter extends WorkoutSessionPresenter<WorkoutHistoryFragmentInterface>
{
    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryFragmentPresenter>
    {
        @Override
        public WorkoutHistoryFragmentPresenter createPresenter()
        {
            return new WorkoutHistoryFragmentPresenter();
        }
    }

    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        mWorkoutSession = Optional.of(workoutSession);

        if (!todaysWorkoutSubscription.isUnsubscribed())
        {
            todaysWorkoutSubscription.unsubscribe();
        }
    }
}
