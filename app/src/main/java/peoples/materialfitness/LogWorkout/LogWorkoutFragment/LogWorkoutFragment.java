package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Navigation.RootFabOnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.R;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.Util.AnimationUtils;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragment extends BaseFragment<LogWorkoutFragmentPresenter>
        implements LogWorkoutFragmentInterface,
        RootFabOnClick,
        ExerciseCardRecyclerAdapter.ExerciseCardAdapterInterface
{
    @Bind(R.id.recycler_empty_view)
    TextView recyclerEmptyView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public PresenterFactory<LogWorkoutFragmentPresenter> getPresenterFactory()
    {
        return new LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterFactory();
    }

    public static LogWorkoutFragment newInstance()
    {
        LogWorkoutFragment fragment = new LogWorkoutFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_log_workout, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (presenter.mWorkoutSession != null &&
                presenter.mWorkoutSession.getExercises().size() > 0)
        {
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(presenter.mWorkoutSession, this));
            recyclerEmptyView.setVisibility(View.GONE);
        }

        // If we have our activity then onAttach has already been called. So we should run this code
        // here instead.
        if (getActivity() != null)
        {
            v.post(this::onViewVisible);
        }

        return v;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        // If getView is not null then onCreateView has already been called.
        if (getView() != null)
        {
            getView().post(this::onViewVisible);
        }
    }

    private void onViewVisible()
    {
        if (getActivity() != null)
        {
            new Handler().postDelayed(((RootFabDisplay)getActivity())::showFab, 500);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onFabClicked(FloatingActionButton fab)
    {
        presenter.onFabClicked();
    }

    @Override
    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        recyclerEmptyView.setVisibility(View.GONE);

        ((ExerciseCardRecyclerAdapter)recyclerView.getAdapter()).updateExerciseCard(exerciseSession);
    }

    @Override
    public void showAddWorkoutDialog()
    {
        LogWorkoutDialog dialog = new LogWorkoutDialog(getActivity(), presenter);
        dialog.show();
    }

    @Override
    public void updateWorkoutList(WorkoutSession workoutSession)
    {
        if (recyclerView.getAdapter() != null)
        {
            ((ExerciseCardRecyclerAdapter)recyclerView.getAdapter()).setWorkoutSession(workoutSession);
        }
        else
        {
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(presenter.mWorkoutSession, this));
            recyclerEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        presenter.onExerciseClicked(session);
    }
}
