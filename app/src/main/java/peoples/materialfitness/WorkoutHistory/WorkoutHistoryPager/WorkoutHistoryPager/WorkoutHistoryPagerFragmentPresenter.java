package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryPager;

import android.content.Context;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.CompleteWorkoutHistoryCache;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryCalendarDialog.WorkoutHistoryCalendarDialogFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutHistoryPagerFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryPagerFragmentInterface>
{
    private List<WorkoutSession> workoutSessions = new ArrayList<WorkoutSession>();

    public WorkoutHistoryPagerFragmentPresenter()
    {
        CompleteWorkoutHistoryCache.getInstance()
                .getAllWorkoutSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(workoutSession -> !DateUtils.isToday(workoutSession.getWorkoutSessionDate()))
                .toList()
                .filter(workoutSessionList -> workoutSessionList.size() > 0)
                .doOnCompleted(() -> fragmentInterface.setWorkoutSessions(workoutSessions))
                .subscribe(workoutSessions -> {
                    WorkoutHistoryPagerFragmentPresenter.this.workoutSessions = workoutSessions;
                    fragmentInterface.setTitle(getWorkoutSessionDateString(workoutSessions.get(0)));
                });

    }

    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryPagerFragmentPresenter>
    {
        @Override
        public WorkoutHistoryPagerFragmentPresenter createPresenter()
        {
            return new WorkoutHistoryPagerFragmentPresenter();
        }
    }

    public List<WorkoutSession> getWorkoutSessions()
    {
        return workoutSessions;
    }

    public void pageChanged(int newPagePosition)
    {
        WorkoutSession newWorkoutSession = workoutSessions.get(newPagePosition);
        String dateString = getWorkoutSessionDateString(newWorkoutSession);
        fragmentInterface.setTitle(dateString);
    }

    private String getWorkoutSessionDateString(WorkoutSession workoutSession)
    {
        String dateString = DateUtils.getShortDateDisplayString(workoutSession.getWorkoutSessionDate());
        return dateString;
    }

    public void calendarMenuClicked()
    {
        fragmentInterface.openDatePickerDialog();
    }

    @Override
    public void onContextAvailable(Context context)
    {
        super.onContextAvailable(context);

        fragmentInterface.hideFab();
    }

    @Subscribe
    public void onWorkoutSelected(WorkoutHistoryCalendarDialogFragment.WorkoutCalendarSessionSelected workoutSessionHolder)
    {
        WorkoutSession workoutSession = workoutSessionHolder.workoutSession;
        int index = workoutSessions.indexOf(workoutSession);
        if (index != -1)
        {
            fragmentInterface.scrollToIndex(index);
        }
    }
}
