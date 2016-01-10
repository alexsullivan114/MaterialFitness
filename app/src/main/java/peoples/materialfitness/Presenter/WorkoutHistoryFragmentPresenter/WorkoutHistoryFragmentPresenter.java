package peoples.materialfitness.Presenter.WorkoutHistoryFragmentPresenter;

import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.WorkoutHistoryFragment.WorkoutHistoryFragmentInterface;

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
