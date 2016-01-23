package peoples.materialfitness.WorkoutHistoryFragment;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

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
