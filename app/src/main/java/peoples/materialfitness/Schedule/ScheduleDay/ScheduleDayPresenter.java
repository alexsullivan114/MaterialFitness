package peoples.materialfitness.Schedule.ScheduleDay;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.Model.WorkoutSession.ScheduleWorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
* Created by Alex Sullivan
*/
public class ScheduleDayPresenter extends BaseActivityPresenter<ScheduleDayInterface>
{
    private ScheduleDay scheduleDay;
    private WorkoutSession workoutSession;

    public static class ScheduleDayPresenterFactory implements PresenterFactory<ScheduleDayPresenter>
    {
        @Override
        public ScheduleDayPresenter createPresenter()
        {
            return new ScheduleDayPresenter();
        }
    }

    public ScheduleDayPresenter()
    {

    }

    public void setScheduleDay(ScheduleDay scheduleDay)
    {
        this.scheduleDay = scheduleDay;
        fetchScheduleDayWorkoutSession();
    }

    private void fetchScheduleDayWorkoutSession()
    {
        new ScheduleWorkoutSessionDatabaseInteractor()
                .fetchWorkoutSessionForScheduleDay(scheduleDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutSession -> {
                    this.workoutSession = workoutSession;
                    if (workoutSession.getExercises().size() > 0)
                    {
                        activityInterface.displayWorkoutSession(workoutSession);
                    }
                    else
                    {
                        activityInterface.showEmptyScreen();
                    }
                });
    }

    public void addExerciseClicked()
    {

    }
}