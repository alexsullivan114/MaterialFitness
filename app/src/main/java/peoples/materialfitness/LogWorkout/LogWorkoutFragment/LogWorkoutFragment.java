package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.AddExerciseDialog;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.WorkoutSession.WorkoutSessionFragment;

/**
 * Created by Alex Sullivan on 4/11/2016.
 * <p>
 * This is the fragment that actually handles allowing a user to add an exercise session.
 */
public class LogWorkoutFragment extends WorkoutSessionFragment<LogWorkoutFragmentPresenter>
        implements LogWorkoutFragmentInterface, AddExerciseDialog.OnExerciseLoggedCallback
{

    public static LogWorkoutFragment newInstance()
    {
        LogWorkoutFragment fragment = new LogWorkoutFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public PresenterFactory<LogWorkoutFragmentPresenter> getPresenterFactory()
    {
        return new LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        fab.setVisibility(View.VISIBLE);
        return v;
    }

    @OnClick(R.id.fab)
    public void onFabClicked()
    {
        presenter.onFabClicked();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.todays_workout);
    }

    @Override
    public void updateWorkoutList(WorkoutSession workoutSession)
    {
        super.updateWorkoutList(workoutSession);

        if (workoutSession.getExerciseSessions().size() == 0)
        {
            recyclerEmptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerEmptyView.setVisibility(View.GONE);
        }
    }

    // TODO: I don't think the presenter should be the callback. The activity should kind of
    // control the interaction of one of its sub views, ya know?
    @Override
    public void showAddWorkoutDialog()
    {
        AddExerciseDialog dialog = new AddExerciseDialog(getActivity(), this);
        dialog.show();
    }

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        presenter.onExerciseLogged(exercise);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleWorkoutDetailsResults(requestCode, resultCode, data);
    }

    @Override
    public void onSpilloverAnimationEnd()
    {
        if (!recyclerView.canScrollVertically(-1))
        {
            fab.show();
        }
    }
}
