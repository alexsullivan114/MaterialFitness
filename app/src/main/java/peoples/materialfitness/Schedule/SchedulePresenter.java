package peoples.materialfitness.Schedule;

import android.support.annotation.ColorInt;
import android.view.View;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ScheduleDay;

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

    public void dayClicked(ScheduleDay day)
    {
        fragmentInterface.startScheduleDayActivity(day);
    }

    public void dayClicked(ScheduleDay day, View transitioningView)
    {
        fragmentInterface.startScheduleDayActivity(day, transitioningView);
    }
}
