package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager;

import java.util.Collections;
import java.util.List;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutHistoryPagerFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryPagerFragmentInterface>
{
    private List<WorkoutSession> workoutSessions = Collections.emptyList();

    public WorkoutHistoryPagerFragmentPresenter()
    {
        String ordering = WorkoutSessionContract.COLUMN_NAME_DATE + " " + ModelDatabaseInteractor.Ordering.DESC.name();
        new WorkoutSessionDatabaseInteractor()
                .fetchAll(null, ordering)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .filter(workoutSessionList -> workoutSessionList.size() > 0)
                .subscribe(workoutSessions -> {
                    WorkoutHistoryPagerFragmentPresenter.this.workoutSessions = workoutSessions;
                    fragmentInterface.setWorkoutSessions(workoutSessions);
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
        String dateString = DateUtils.getShortDateDisplaySTring(workoutSession.getWorkoutSessionDate());
        return dateString;
    }
}
