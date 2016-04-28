package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;

/**
 * Created by Alex Sullivan on 2/15/16.
 */


public abstract class WorkoutDetailsActivity<T extends WorkoutDetailsPresenter> extends BaseActivity<T>
        implements WorkoutDetailsActivityInterface, WorkoutDetailsRecyclerAdapter.SetInteractionCallback
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    ExerciseGraph chart;
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    protected @Bind(R.id.middleFab)
    FloatingActionButton middleFab;
    @Bind(R.id.appBar)
    protected AppBarLayout appBarLayout;
    protected @Bind(R.id.bottomFab)
    FloatingActionButton bottomFab;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_details);
        ButterKnife.bind(this);

        presenter.setBundle(getIntent().getExtras());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WorkoutDetailsRecyclerAdapter(presenter.exerciseSession, this, allowSetTouchEvents()));
    }

    @Override
    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setChartData(List<WorkoutSession> workoutSessionList, Exercise exercise)
    {
        chart.setExercise(exercise);
        chart.setWorkoutSessions(workoutSessionList);
    }

    @Override
    public void refreshSets()
    {
        ((WorkoutDetailsRecyclerAdapter)recyclerView.getAdapter()).setExerciseSession(presenter.exerciseSession);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void removeSetAtPosition(int position)
    {
        ((WorkoutDetailsRecyclerAdapter)recyclerView.getAdapter()).setExerciseSession(presenter.exerciseSession);
        recyclerView.getAdapter().notifyItemRemoved(position);
        recyclerView.getAdapter().notifyItemRangeChanged(position, presenter.exerciseSession.getSets().size() - 1);
    }

    @Override
    public void contentUpdated(boolean didUpdate)
    {
        setResult(WorkoutSessionPresenter.WORKOUT_DETAILS_CONTENT_UPDATED);
    }

    @Override
    public void hideSetOptions()
    {
        ((WorkoutDetailsRecyclerAdapter)recyclerView.getAdapter()).hideSetOptions();
    }

    @Override
    public void deleteButtonClicked(int position)
    {
        presenter.deleteSetButtonClicked(position);
    }

    @Override
    public void editButtonClicked(int position)
    {
        presenter.editSetButtonClicked(position);
    }

    protected abstract boolean allowSetTouchEvents();
}
