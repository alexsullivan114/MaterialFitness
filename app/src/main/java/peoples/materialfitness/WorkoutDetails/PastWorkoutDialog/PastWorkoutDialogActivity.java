package peoples.materialfitness.WorkoutDetails.PastWorkoutDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 5/4/2016.
 */
public class PastWorkoutDialogActivity extends BaseActivity<PastWorkoutDialogPresenter>
        implements PastWorkoutDialogInterface
{
    private static final String WORKOUT_SESSION_DATE_KEY = "workoutSessionDateKey";
    private static final String EXERCISE_SESSION_KEY = "exerciseSessionKey";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static Intent getIntent(long workoutSessionDate,
                                   ExerciseSession exerciseSession,
                                   Context startingContext)

    {
        Intent intent = new Intent(startingContext, PastWorkoutDialogActivity.class);

        intent.putExtra(WORKOUT_SESSION_DATE_KEY, workoutSessionDate);
        intent.putExtra(EXERCISE_SESSION_KEY, Parcels.wrap(exerciseSession));

        return intent;
    }

    @Override
    protected PresenterFactory<PastWorkoutDialogPresenter> getPresenterFactory()
    {
        return new PastWorkoutDialogPresenter.PastWorkoutDialogPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_workout_details);
        ButterKnife.bind(this);

        setPresenterArguments();

        setTitle(presenter.getTitle());
    }

    private void setPresenterArguments()
    {
        presenter.setWorkoutSessionDate(getIntent().getLongExtra(WORKOUT_SESSION_DATE_KEY, -1));
        presenter.setExerciseSession(Parcels.unwrap(getIntent().getParcelableExtra(EXERCISE_SESSION_KEY)));
    }

    @Override
    public void setupRecyclerView(ExerciseSession exerciseSession)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PastWorkoutDialogRecyclerAdapter(exerciseSession));
    }

    @Override
    public void setWeightSetAsPr(WeightSet weightSet)
    {
        ((PastWorkoutDialogRecyclerAdapter)recyclerView.getAdapter()).setWeightSetAsPr(weightSet);
    }
}
