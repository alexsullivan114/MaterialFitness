package peoples.materialfitness.Schedule;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

/**
 * Created by Alex Sullivan on 5/14/2016.
 */
public class SchedulePresenter extends BaseFragmentPresenter<ScheduleViewInterface>
{
    public static class SchedulePresenterFactory implements PresenterFactory<SchedulePresenter>
    {
        @Override
        public SchedulePresenter createPresenter()
        {
            return new SchedulePresenter();
        }
    }
}
