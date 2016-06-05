package peoples.materialfitness.Schedule.ScheduleDay;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.AddExerciseDialog;
import peoples.materialfitness.LogWorkout.LogWorkoutFragment.ExerciseCardRecyclerAdapter;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.AnimationUtils;
import peoples.materialfitness.Util.AnimationHelpers.TransitionListenerAdapter;
import peoples.materialfitness.Util.CustomAnimations.StatusBarColorTransition;
import peoples.materialfitness.Util.VersionUtils;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan
 */
public class ScheduleDayActivity extends BaseActivity<ScheduleDayPresenter>
        implements ScheduleDayInterface,
                   ExerciseCardRecyclerAdapter.ExerciseCardAdapterInterface,
                   AddExerciseDialog.OnExerciseLoggedCallback
{
    private static final String SCHEDULE_DAY_EXTRA = "scheduleDayExtra";
    private static final String TRANSITION_NAME_EXTRA = "transitionNameExtra";

    private boolean hasFinishedEnterTransition = false;
    private boolean showFab = false;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_mask)
    View toolbarMask;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.recycler_empty_view)
    TextView recyclerEmptyView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ScheduleDay scheduleDay;

    @Override
    protected PresenterFactory<ScheduleDayPresenter> getPresenterFactory()
    {
        return new ScheduleDayPresenter.ScheduleDayPresenterFactory();
    }

    public static Intent getStartingIntent(Context context, ScheduleDay scheduleDay)
    {
        Intent intent = new Intent(context, ScheduleDayActivity.class);
        intent.putExtra(SCHEDULE_DAY_EXTRA, scheduleDay);
        return intent;
    }

    public static Intent getStartingIntent(Context context, ScheduleDay scheduleDay,
                                           String transitionName)
    {
        Intent intent = new Intent(context, ScheduleDayActivity.class);
        intent.putExtra(SCHEDULE_DAY_EXTRA, scheduleDay);
        intent.putExtra(TRANSITION_NAME_EXTRA, transitionName);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_day);
        ButterKnife.bind(this);

        scheduleDay = (ScheduleDay) getIntent().getSerializableExtra(SCHEDULE_DAY_EXTRA);
        presenter.setScheduleDay(scheduleDay);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle(scheduleDay.getDisplayName());

        // Handle our entering transition if we're not restoring ourselves.
        if (getIntent().hasExtra(TRANSITION_NAME_EXTRA) && savedInstanceState == null)
        {
            setupTransition();
        }

        restoreInstanceState(savedInstanceState);
    }

    private void restoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            toolbarMask.setAlpha(0.0f);
            toolbar.setBackgroundColor(getResources().getColor(scheduleDay.getColorResInt()));

            if (VersionUtils.isLollipopOrGreater())
            {
                getWindow().setStatusBarColor(getResources().getColor(scheduleDay.getPressedColorRes()));
            }
        }
    }

    private String getEmptyString()
    {
        String scheduleDayString = getResources().getString(scheduleDay.getDisplayName());
        String formattedString = getResources().getString(R.string.schedule_empty, scheduleDayString);

        return formattedString;
    }

    @Override
    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        ExerciseCardRecyclerAdapter adapter = new ExerciseCardRecyclerAdapter(workoutSession, this);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    protected void addExerciseClicked()
    {
        presenter.addExerciseClicked();
    }

    @Override
    public void displayWorkoutSession(WorkoutSession workoutSession)
    {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        // no-op.
    }

    @Override
    public void onSpilloverAnimationEnd()
    {
        // no-op.
    }

    @Override
    public void showEmptyScreen()
    {
        recyclerView.setVisibility(View.GONE);
        recyclerEmptyView.setVisibility(View.VISIBLE);
        recyclerEmptyView.setText(getEmptyString());
    }

    @Override
    public void showAddExerciseDialog()
    {
        new AddExerciseDialog(this, this)
                .show();
    }

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        presenter.exerciseLogged(exercise);
    }

    @Override
    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        recyclerEmptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        ((ExerciseCardRecyclerAdapter) recyclerView.getAdapter()).updateExerciseCard(exerciseSession);
    }

    @Override
    public void showFab()
    {
        if (!VersionUtils.isLollipopOrGreater() || hasFinishedEnterTransition)
        {
            fab.show();
        }
        else if (!hasFinishedEnterTransition)
        {
            showFab = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupTransition()
    {
        setupEnterTransition();
        setupReturnTransition();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterTransition()
    {
        Intent startingIntent = getIntent();

        String transitionName = startingIntent.getStringExtra(TRANSITION_NAME_EXTRA);
        @ColorInt int backgroundColor = getResources().getColor(scheduleDay.getColorResInt());
        @ColorInt int darkScheduleColor = getResources().getColor(scheduleDay.getPressedColorRes());
        @ColorInt int statusBarColor = getWindow().getStatusBarColor();

        toolbar.setBackgroundColor(backgroundColor);
        toolbar.setAlpha(0.0f);
        toolbarMask.setTransitionName(transitionName);
        toolbarMask.setBackgroundColor(backgroundColor);

        Transition sharedElementTransition = getWindow().getEnterTransition();
        Transition statusBarColorTransition = new StatusBarColorTransition(statusBarColor, darkScheduleColor, getWindow());

        TransitionSet set = new TransitionSet();
        set.addTransition(sharedElementTransition);
        set.addTransition(statusBarColorTransition);

        getWindow().setEnterTransition(set);

        sharedElementTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        sharedElementTransition.excludeTarget(android.R.id.statusBarBackground, true);
        sharedElementTransition.addListener(new TransitionListenerAdapter()
        {
            @Override
            public void onTransitionStart(Transition transition)
            {
                toolbar.setAlpha(0.0f);
            }

            @Override
            public void onTransitionEnd(Transition transition)
            {
                toolbar.setAlpha(1.0f);
                AnimationUtils.fadeOutView(toolbarMask);
                getWindow().setStatusBarColor(getResources().getColor(scheduleDay.getPressedColorRes()));

                if (showFab)
                {
                    fab.show();
                }
            }
        });

        sharedElementTransition.addListener(new TransitionListenerAdapter()
        {
            @Override
            public void onTransitionStart(Transition transition)
            {
                toolbarMask.setAlpha(1.0f);
                toolbarMask.setVisibility(View.VISIBLE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupReturnTransition()
    {
        TransitionSet transitionSet = new TransitionSet();
        Transition transition = getWindow().getReturnTransition();

        transitionSet.addTransition(transition);
        transitionSet.addListener(new TransitionListenerAdapter()
        {
            @Override
            public void onTransitionEnd(Transition transition)
            {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        getWindow().setReturnTransition(transitionSet);
    }
}