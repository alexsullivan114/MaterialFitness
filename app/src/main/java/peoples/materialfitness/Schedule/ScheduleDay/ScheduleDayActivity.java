package peoples.materialfitness.Schedule.ScheduleDay;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ScheduleDay;
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
        implements ScheduleDayInterface
{
    private static final String SCHEDULE_DAY_EXTRA = "scheduleDayExtra";
    private static final String TRANSITION_NAME_EXTRA = "transitionNameExtra";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_mask)
    View toolbarMask;

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

        scheduleDay = (ScheduleDay)getIntent().getSerializableExtra(SCHEDULE_DAY_EXTRA);

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupTransition()
    {
        setupEnterTransition();
//        setupReenterTransition();
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
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void setupReenterTransition()
//    {
//        @ColorInt int primaryDarkColor = getResources().getColor(R.color.colorPrimaryDark);
//
//        Transition sharedElementTransition = getWindow().getEnterTransition();
//        Transition statusBarColorTransition = new StatusBarColorTransition(getWindow().getStatusBarColor(), primaryDarkColor, getWindow());
//
//        TransitionSet set = new TransitionSet();
//        set.addTransition(sharedElementTransition);
//        set.addTransition(statusBarColorTransition);
//
//        getWindow().setReturnTransition(set);
//    }
}