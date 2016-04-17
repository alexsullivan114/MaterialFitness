package peoples.materialfitness.WorkoutSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.LogWorkout.LogWorkoutFragment.ExerciseCardRecyclerAdapter;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public abstract class WorkoutSessionFragment<T extends WorkoutSessionPresenter> extends BaseFragment<T>
        implements WorkoutSessionFragmentInterface,
                   ExerciseCardRecyclerAdapter.ExerciseCardAdapterInterface
{

    @Bind(R.id.recycler_empty_view)
    TextView recyclerEmptyView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_log_workout, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (presenter.getWorkoutSession().isPresent() &&
                ((WorkoutSession)presenter.getWorkoutSession().get()).getExercises().size() > 0)
        {
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(((WorkoutSession)presenter.getWorkoutSession().get()), this));
            recyclerEmptyView.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        recyclerEmptyView.setVisibility(View.GONE);

        ((ExerciseCardRecyclerAdapter)recyclerView.getAdapter()).updateExerciseCard(exerciseSession);
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
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(((WorkoutSession)presenter.getWorkoutSession().get()), this));
            recyclerEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        presenter.onExerciseClicked(session);
    }

    @Override
    public void startWorkoutDetailsActivity(Intent startingIntent, int workoutDetailsRequestCode)
    {
        startActivityForResult(startingIntent, workoutDetailsRequestCode);
    }
}
