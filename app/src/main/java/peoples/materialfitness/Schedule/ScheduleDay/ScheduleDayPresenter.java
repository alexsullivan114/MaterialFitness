package peoples.materialfitness.Schedule.ScheduleDay;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

/**
* Created by Alex Sullivan
*/
public class ScheduleDayPresenter extends BaseActivityPresenter<ScheduleDayInterface>
{
    public static class ScheduleDayPresenterFactory implements PresenterFactory<ScheduleDayPresenter>
    {
        @Override
        public ScheduleDayPresenter createPresenter()
        {
            return new ScheduleDayPresenter();
        }
    }
}