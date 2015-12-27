package peoples.materialfitness.Presenter.WorkoutHistoryFragmentPresenter;

import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.WorkoutHistoryFragment.WorkoutHistoryFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutHistoryFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryFragmentInterface>
        implements WorkoutHistoryFragmentPresenterInterface
{
    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryFragmentPresenterInterface>
    {
        @Override
        public WorkoutHistoryFragmentPresenterInterface createPresenter()
        {
            return new WorkoutHistoryFragmentPresenter();
        }
    }
}
