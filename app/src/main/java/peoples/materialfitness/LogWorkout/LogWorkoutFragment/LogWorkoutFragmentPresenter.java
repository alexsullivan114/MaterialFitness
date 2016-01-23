package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

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
