package peoples.materialfitness.Schedule.ScheduleDay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.AnimationUtils;
import peoples.materialfitness.Util.AnimationHelpers.TransitionListenerAdapter;
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

        if (getIntent().hasExtra(TRANSITION_NAME_EXTRA))
        {
            setupTransition();
        }
    }

    @SuppressLint("NewApi")
    private void setupTransition()
    {
        Intent startingIntent = getIntent();

        String transitionName = startingIntent.getStringExtra(TRANSITION_NAME_EXTRA);
        @ColorInt int backgroundColor = getResources().getColor(scheduleDay.getColorResInt());

        toolbar.setBackgroundColor(backgroundColor);
        toolbar.setAlpha(0.0f);
        toolbarMask.setTransitionName(transitionName);
        toolbarMask.setBackgroundColor(backgroundColor);

        getWindow().getEnterTransition().addListener(new TransitionListenerAdapter()
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
                getWindow().setStatusBarColor(getColor(scheduleDay.getPressedColorRes()));
            }
        });

        getWindow().getEnterTransition().excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().getReturnTransition().addListener(new TransitionListenerAdapter()
        {
            @Override
            public void onTransitionStart(Transition transition)
            {
                toolbarMask.setAlpha(1.0f);
                toolbarMask.setVisibility(View.VISIBLE);
            }
        });
    }
}