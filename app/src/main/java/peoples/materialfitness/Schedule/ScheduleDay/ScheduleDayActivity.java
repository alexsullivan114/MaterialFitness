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
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.AddExerciseDialog;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan
 */
public class ScheduleDayActivity extends BaseActivity<ScheduleDayPresenter>
        implements ScheduleDayInterface,
                   AddExerciseDialog.OnExerciseLoggedCallback,
                   ScheduleDayRecyclerAdapter.ScheduleDayAdapterCallback
{
    private static final String SCHEDULE_DAY_EXTRA = "scheduleDayExtra";
    private static final String TRANSITION_NAME_EXTRA = "transitionNameExtra";
    private static final int LOG_ALL_ID = 99;

    private boolean hasFinishedEnterTransition = false;
    private List<Runnable> viewRunnable = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, LOG_ALL_ID, Menu.NONE, R.string.toolbar_log_all)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == LOG_ALL_ID)
        {
            presenter.logAllClicked();
        }

        return super.onOptionsItemSelected(item);
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

            presenter.getWorkoutSession()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::immediateDisplayWorkoutSession);
        }
    }

    private String getEmptyString()
    {
        String scheduleDayString = getResources().getString(scheduleDay.getDisplayName());

        return getResources().getString(R.string.schedule_empty, scheduleDayString);
    }

    @Override
    public void removeExercise(int position)
    {
        ((ScheduleDayRecyclerAdapter)recyclerView.getAdapter()).removeExercise(position);
        if (recyclerView.getAdapter().getItemCount() == 0)
        {
            immediateShowEmptyScreen();
        }
    }

    @OnClick(R.id.fab)
    protected void addExerciseClicked()
    {
        presenter.addExerciseClicked();
    }

    @Override
    public void itemDeleted(int position)
    {
        presenter.itemDeleted(position);
    }

    @Override
    public void displayWorkoutSession(WorkoutSession workoutSession)
    {
        Log.d(TAG, "Called display workout session...");
        executeViewCodeAfterTransition(() -> {

            ScheduleDayRecyclerAdapter adapter = new ScheduleDayRecyclerAdapter(workoutSession.getExerciseList(), this);
            recyclerView.setAdapter(adapter);

            if (workoutSession.getExerciseSessions().size() > 0)
            {
                Log.d(TAG, "Fading recycler view in...");
                immediateDisplayWorkoutSession(workoutSession);
            }
            else if (recyclerEmptyView.getVisibility() != View.VISIBLE)
            {
                immediateShowEmptyScreen();
            }
        });
    }

    private void immediateDisplayWorkoutSession(WorkoutSession workoutSession)
    {
        AnimationUtils.fadeVisibilityChange(recyclerView, View.VISIBLE);
        AnimationUtils.fadeVisibilityChange(recyclerEmptyView, View.GONE);
    }

    private void showEmptyScreen()
    {
        executeViewCodeAfterTransition(this::immediateShowEmptyScreen);
    }

    private void immediateShowEmptyScreen()
    {
        recyclerView.setVisibility(View.GONE);
        recyclerEmptyView.setVisibility(View.VISIBLE);
        recyclerEmptyView.setAlpha(1.0f);
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
        recyclerView.setAlpha(1.0f);
        ((ScheduleDayRecyclerAdapter) recyclerView.getAdapter()).addExercise(exerciseSession.getExercise());
    }

    @Override
    public void showFab()
    {
        executeViewCodeAfterTransition(() -> fab.show());
    }

    private void executeViewCodeAfterTransition(Runnable runnable)
    {
        if (!VersionUtils.isLollipopOrGreater() || hasFinishedEnterTransition)
        {
            runnable.run();
        }
        else
        {
            viewRunnable.add(runnable);
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
        toolbarMask.setVisibility(View.VISIBLE);
        toolbarMask.setTransitionName(transitionName);
        toolbarMask.setBackgroundColor(backgroundColor);

        fab.setVisibility(View.INVISIBLE);

        recyclerView.setAlpha(0);

        Transition sharedElementTransition = getWindow().getEnterTransition();
        Transition statusBarColorTransition = new StatusBarColorTransition(statusBarColor, darkScheduleColor, getWindow());
        Transition fade = new Fade(Fade.IN);
        fade.addTarget(R.id.recycler_empty_view);
        fade.addTarget(R.id.recyclerView);

        TransitionSet set = new TransitionSet();
        set.addTransition(sharedElementTransition);
        set.addTransition(statusBarColorTransition);
        set.addTransition(fade);

        getWindow().setEnterTransition(set);

        sharedElementTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        sharedElementTransition.excludeTarget(android.R.id.statusBarBackground, true);
        sharedElementTransition.addListener(new TransitionListenerAdapter()
        {
            @Override
            public void onTransitionStart(Transition transition)
            {
                Log.d(TAG, "onTransitionStart: ");
                toolbar.setAlpha(0.0f);
            }

            @Override
            public void onTransitionEnd(Transition transition)
            {
                Log.d(TAG, "onTransitionEnd: ");
                toolbar.setAlpha(1.0f);
                AnimationUtils.fadeOutView(toolbarMask);
                getWindow().setStatusBarColor(getResources().getColor(scheduleDay.getPressedColorRes()));

                hasFinishedEnterTransition = true;

                if (viewRunnable != null)
                {
                    for (Runnable runnable : viewRunnable)
                    {
                        runnable.run();
                    }

                    viewRunnable.clear();
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