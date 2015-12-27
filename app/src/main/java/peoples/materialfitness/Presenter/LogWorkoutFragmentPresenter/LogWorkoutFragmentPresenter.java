package peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter;

import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
    implements LogWorkoutFragmentPresenterInterface<LogWorkoutFragmentInterface>
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenterInterface>
    {
        @Override
        public LogWorkoutFragmentPresenterInterface createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    @Override
    public void onFabClicked()
    {

    }
}
