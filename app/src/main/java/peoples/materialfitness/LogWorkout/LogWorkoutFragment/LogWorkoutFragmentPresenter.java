package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import peoples.materialfitness.Presenter.CorePresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }
}
