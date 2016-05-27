package peoples.materialfitness.Schedule;

import android.support.annotation.ColorInt;
import android.view.View;

import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 5/14/2016.
 */
public interface ScheduleViewInterface extends BaseFragmentInterface
{
    void startScheduleDayActivity(ScheduleDay scheduleDay);
    void startScheduleDayActivity(ScheduleDay scheduleDay, View transitioningView);
}
