package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Navigation.RootFabOnClick;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.WorkoutSession.WorkoutSessionFragment;

/**
 * Created by Alex Sullivan on 4/11/2016.
 *
 * This is the fragment that actually handles allowing a user to add an exercise session.
 */
public class LogWorkoutFragment extends WorkoutSessionFragment<LogWorkoutFragmentPresenter> implements
        RootFabOnClick,
        LogWorkoutFragmentInterface
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

        // If we have our activity then onAttach has already been called. So we should run this code
        // here instead.
        if (getActivity() != null && v != null)
        {
            v.post(this::onViewVisible);
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // If getView is not null then onCreateView has already been called.
        if (getView() != null)
        {
            getView().post(this::onViewVisible);
        }

        ((BaseActivity)getActivity()).getSupportActionBar().setTitle(R.string.todays_workout);
    }

    @Override
    public void updateWorkoutList(WorkoutSession workoutSession)
    {
        super.updateWorkoutList(workoutSession);

        if (workoutSession.getExercises().size() == 0)
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

    @Override
    public void showAddWorkoutDialog()
    {
        LogWorkoutDialog dialog = new LogWorkoutDialog(getActivity(), presenter);
        dialog.show();
    }

    @Override
    public void onFabClicked(FloatingActionButton fab)
    {
        presenter.onFabClicked();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleWorkoutDetailsResults(requestCode, resultCode, data);
    }

    private void onViewVisible()
    {
        if (getActivity() != null)
        {
            new Handler().postDelayed(((RootFabDisplay)getActivity())::showFab, 500);
        }
    }

    @Override
    protected void onPositiveScroll()
    {
        super.onPositiveScroll();
        ((RootFabDisplay)getActivity()).showFab();
    }

    @Override
    protected void onNegativeScroll()
    {
        super.onNegativeScroll();
        ((RootFabDisplay)getActivity()).hideFab();
    }

    @Override
    public void onSpilloverAnimationEnd()
    {
        if (!recyclerView.canScrollVertically(-1))
        {
            ((RootFabDisplay)getActivity()).showFab();
        }
    }
}
