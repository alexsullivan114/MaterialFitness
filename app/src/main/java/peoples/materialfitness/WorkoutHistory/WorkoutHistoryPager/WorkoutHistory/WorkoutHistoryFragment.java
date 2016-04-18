package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistory;

import android.os.Bundle;

import org.parceler.Parcels;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.WorkoutSession.WorkoutSessionFragment;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class WorkoutHistoryFragment extends WorkoutSessionFragment<WorkoutHistoryFragmentPresenter>
{
    public static final String WORKOUT_SESSION_KEY = "workoutSessionKey";

    @Override
    protected PresenterFactory<WorkoutHistoryFragmentPresenter> getPresenterFactory()
    {
        return new WorkoutHistoryFragmentPresenter.WorkoutHistoryFragmentPresenterFactory();
    }

    public static WorkoutHistoryFragment newInstance(WorkoutSession workoutSession)
    {
        WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKOUT_SESSION_KEY, Parcels.wrap(workoutSession));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WorkoutSession workoutSession = Parcels.unwrap(getArguments().getParcelable(WORKOUT_SESSION_KEY));
        presenter.setWorkoutSession(workoutSession);
    }

    @Override
    public void onSpilloverAnimationEnd()
    {
        // don't care.
    }
}
