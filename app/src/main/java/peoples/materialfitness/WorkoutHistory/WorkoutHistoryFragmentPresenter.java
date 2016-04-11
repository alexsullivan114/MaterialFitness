package peoples.materialfitness.WorkoutHistory;

import java.util.Collections;
import java.util.List;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutHistoryFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryFragmentInterface>
{
    private List<WorkoutSession> workoutSessions = Collections.emptyList();

    public WorkoutHistoryFragmentPresenter()
    {
        String ordering = WorkoutSessionContract.COLUMN_NAME_DATE + " " + ModelDatabaseInteractor.Ordering.DESC.name();
        new WorkoutSessionDatabaseInteractor()
                .fetchAll(null, ordering)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(workoutSessions -> {
                    WorkoutHistoryFragmentPresenter.this.workoutSessions = workoutSessions;
                    fragmentInterface.setWorkoutSessions(workoutSessions);
                });

    }

    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryFragmentPresenter>
    {
        @Override
        public WorkoutHistoryFragmentPresenter createPresenter()
        {
            return new WorkoutHistoryFragmentPresenter();
        }
    }

    public List<WorkoutSession> getWorkoutSessions()
    {
        return workoutSessions;
    }
}
