package peoples.materialfitness.LogWorkout.WorkoutCardFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragmentPresenter;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 1/23/16.
 */
public class WorkoutCardFragment extends BaseFragment<WorkoutCardFragmentPresenter>
        implements WorkoutCardFragmentInterface
{
    private static final String EXERCISE_SESSION_INTENT_KEY = "exerciseSessionKey";

    private ExerciseSession exerciseSession;

    @Override
    protected PresenterFactory<WorkoutCardFragmentPresenter> getPresenterFactory()
    {
        return new WorkoutCardFragmentPresenter.WorkoutCardFragmentPresenterFactory();
    }

    public static WorkoutCardFragment newInstance(ExerciseSession exerciseSession)
    {
        WorkoutCardFragment fragment = new WorkoutCardFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXERCISE_SESSION_INTENT_KEY, Parcels.wrap(exerciseSession));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_workout_card, null);

        return v;
    }
}
