package peoples.materialfitness.WorkoutSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
    protected TextView recyclerEmptyView;
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @Bind(R.id.root_view)
    protected RelativeLayout rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_log_workout, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0)
                {
                    onPositiveScroll();
                }
                else
                {
                    onNegativeScroll();
                }
            }
        });

        if (presenter.getWorkoutSession().isPresent() &&
                ((WorkoutSession) presenter.getWorkoutSession().get()).getExercises().size() > 0)
        {
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(((WorkoutSession) presenter.getWorkoutSession().get()), this));
            recyclerEmptyView.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        presenter.onExerciseClicked(session);
    }

    @Override
    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        recyclerEmptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ((ExerciseCardRecyclerAdapter) recyclerView.getAdapter()).updateExerciseCard(exerciseSession);
    }

    @Override
    public void updateWorkoutList(WorkoutSession workoutSession)
    {
        if (recyclerView.getAdapter() != null)
        {
            ((ExerciseCardRecyclerAdapter) recyclerView.getAdapter()).setWorkoutSession(workoutSession);
        }
        else
        {
            recyclerView.setAdapter(new ExerciseCardRecyclerAdapter(((WorkoutSession) presenter.getWorkoutSession().get()), this));
            recyclerEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void startWorkoutDetailsActivity(Intent startingIntent, int workoutDetailsRequestCode)
    {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(), getSharedElements()).toBundle();
        startActivityForResult(startingIntent, workoutDetailsRequestCode, bundle);
    }

    protected Pair<View, String>[] getSharedElements()
    {
        return null;
    }

    /**
     * Called when the associated recyclerview scrolls up
     */
    protected void onPositiveScroll()
    {

    }

    /**
     * Called when the associated recyclerview scrolls down
     */
    protected void onNegativeScroll()
    {

    }

}
