package peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.TransitionListenerAdapter;
import peoples.materialfitness.Util.CustomAnimations.CircularRevealTransition;
import peoples.materialfitness.Util.VersionUtils;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.WorkoutDetails.ExerciseGraph.ExerciseGraph;
import peoples.materialfitness.WorkoutDetails.PastWorkoutDialog.PastWorkoutDialogActivity;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;

/**
 * Created by Alex Sullivan on 2/15/16.
 */


public abstract class WorkoutDetailsActivity<T extends WorkoutDetailsPresenter> extends BaseActivity<T>
        implements WorkoutDetailsActivityInterface,
                   WorkoutDetailsRecyclerAdapter.SetInteractionCallback,
                   ExerciseGraph.InteractionCallback
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    ExerciseGraph chart;
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @Bind(R.id.middleFab)
    protected FloatingActionButton middleFab;
    @Bind(R.id.appBar)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.bottomFab)
    protected FloatingActionButton bottomFab;

    private static final String IS_UPDATED_KEY = "isUpdatedKey";

    private boolean contentUpdated = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        ButterKnife.bind(this);
        setTransitions();

        presenter.setBundle(getIntent().getExtras());
        chart.setCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WorkoutDetailsRecyclerAdapter(presenter.exerciseSession, this, allowSetTouchEvents()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(presenter.exerciseSession));
        outState.putBoolean(IS_UPDATED_KEY, contentUpdated);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void exerciseClicked(ExerciseSession exerciseSession, long exerciseSessionDate)
    {
        Intent intent = PastWorkoutDialogActivity.getIntent(exerciseSessionDate, exerciseSession, this);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_in_top);
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
        ((WorkoutDetailsRecyclerAdapter) recyclerView.getAdapter()).setExerciseSession(presenter.exerciseSession);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void removeSetAtPosition(int position)
    {
        ((WorkoutDetailsRecyclerAdapter) recyclerView.getAdapter()).setExerciseSession(presenter.exerciseSession);
        recyclerView.getAdapter().notifyItemRemoved(position);
        recyclerView.getAdapter().notifyItemRangeChanged(position, presenter.exerciseSession.getSets().size());
    }

    @Override
    public void refreshSetAtPosition(int position)
    {
        recyclerView.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void contentUpdated(boolean didUpdate)
    {
        contentUpdated = true;
        setResult(WorkoutSessionPresenter.WORKOUT_DETAILS_CONTENT_UPDATED);
    }

    @Override
    public void hideSetOptions()
    {
        ((WorkoutDetailsRecyclerAdapter) recyclerView.getAdapter()).hideSetOptions();
    }

    @Override
    public void showEditWeightSetDialog(double weight, int reps)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.edit_set)
                .customView(R.layout.add_rep_dialog, false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive((dialog1, which) -> {
                    View customView = dialog1.getCustomView();
                    EditText repsText = (EditText) customView.findViewById(R.id.reps);
                    EditText weightText = (EditText) customView.findViewById(R.id.weight);
                    int repsInt = Integer.parseInt(repsText.getText().toString());
                    int weightInt = Integer.parseInt(weightText.getText().toString());
                    presenter.editSet(weightInt, repsInt);
                })
                .onNegative((dialog1, which) -> {
                    presenter.abandonEditing();
                })
                .build();

        EditText repEditText = (EditText) dialog.findViewById(R.id.reps);
        EditText weightEditText = (EditText) dialog.findViewById(R.id.weight);

        repEditText.append(String.valueOf(reps));
        weightEditText.append(String.format(Locale.getDefault(), "%.1f", weight));

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
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

    @Override
    public void setWeightSetAsPr(WeightSet weightSet)
    {
        if (recyclerView.getAdapter() != null)
        {
            ((WorkoutDetailsRecyclerAdapter)recyclerView.getAdapter()).setWeightSetAsPr(weightSet);
        }
    }

    @SuppressLint("NewApi")
    private void setEnterTransition()
    {
        if (VersionUtils.isLollipopOrGreater())
        {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.recyclerView);

            Fade chartFade = new Fade(Fade.IN);
            chartFade.addTarget(R.id.appBar);

            CircularRevealTransition revealTransition = new CircularRevealTransition(0, 0);
            revealTransition.addTarget(R.id.appBar);

            Fade recyclerFade = new Fade(Fade.IN);
            recyclerFade.addTarget(R.id.recyclerView);
            recyclerFade.setStartDelay(150);

            TransitionSet chartRevealTransition = new TransitionSet();
            chartRevealTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
            chartRevealTransition.addTransition(revealTransition);
            chartRevealTransition.addTransition(chartFade);

            TransitionSet recyclerTransition = new TransitionSet();
            recyclerTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
            recyclerTransition.addTransition(slide);
            recyclerTransition.addTransition(recyclerFade);

            TransitionSet set = new TransitionSet();
            set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
            set.addTransition(chartRevealTransition);
            set.addTransition(recyclerTransition);
            set.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
            set.addListener(new TransitionListenerAdapter()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {
                    middleFab.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onTransitionEnd(Transition transition)
                {
                    if (getFabVisibility() == View.VISIBLE)
                    {
                        middleFab.show();
                    }
                }
            });

            getWindow().setEnterTransition(set);
        }
    }

    @SuppressLint("NewApi")
    private void setExitTransition()
    {
        if (VersionUtils.isLollipopOrGreater())
        {
            Slide slideBottom = new Slide(Gravity.BOTTOM);
            slideBottom.addTarget(recyclerView);
            Slide slideTop = new Slide(Gravity.TOP);
            slideTop.addTarget(appBarLayout);
            Fade fade = new Fade(Fade.OUT);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);

            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(slideBottom);
            transitionSet.addTransition(slideTop);
            transitionSet.addTransition(fade);

            getWindow().setReturnTransition(transitionSet);
        }
    }

    private void setTransitions()
    {
        if (VersionUtils.isLollipopOrGreater())
        {
            recyclerView.setTransitionGroup(true);
        }
        setEnterTransition();
        setExitTransition();
    }

    protected abstract boolean allowSetTouchEvents();
    protected abstract int getFabVisibility();
}
