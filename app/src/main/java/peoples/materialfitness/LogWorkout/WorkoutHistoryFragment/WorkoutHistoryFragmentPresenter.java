package peoples.materialfitness.LogWorkout.WorkoutHistoryFragment;

import peoples.materialfitness.Presenter.CorePresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutHistoryFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryFragmentInterface>
{
    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryFragmentPresenter>
    {
        @Override
        public WorkoutHistoryFragmentPresenter createPresenter()
        {
            return new WorkoutHistoryFragmentPresenter();
        }
    }
}
