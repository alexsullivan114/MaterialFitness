package peoples.materialfitness.LogWorkout.WorkoutCardFragment;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

/**
 * Created by Alex Sullivan on 1/23/16.
 */
public class WorkoutCardFragmentPresenter extends BaseFragmentPresenter<WorkoutCardFragmentInterface>
{
    public static class WorkoutCardFragmentPresenterFactory implements PresenterFactory<WorkoutCardFragmentPresenter>
    {
        @Override
        public WorkoutCardFragmentPresenter createPresenter()
        {
            return new WorkoutCardFragmentPresenter();
        }
    }
}
