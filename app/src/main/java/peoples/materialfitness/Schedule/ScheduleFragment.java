package peoples.materialfitness.Schedule;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.R;
import peoples.materialfitness.Schedule.ScheduleDay.ScheduleDayActivity;
import peoples.materialfitness.Util.CustomAnimations.StatusBarColorTransition;
import peoples.materialfitness.Util.VersionUtils;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 5/14/2016.
 */
public class ScheduleFragment extends BaseFragment<SchedulePresenter> implements ScheduleViewInterface
{
    @Bind(R.id.monday_view)
    TextView mondayView;
    @Bind(R.id.tuesday_view)
    TextView tuesdayView;
    @Bind(R.id.wednesday_view)
    TextView wednesdayView;
    @Bind(R.id.thursday_view)
    TextView thursdayView;
    @Bind(R.id.friday_view)
    TextView fridayView;
    @Bind(R.id.saturday_view)
    TextView saturdayView;
    @Bind(R.id.sunday_view)
    TextView sundayView;

    public static ScheduleFragment newInstance()
    {
        return new ScheduleFragment();
    }

    @Override
    protected PresenterFactory<SchedulePresenter> getPresenterFactory()
    {
        return new SchedulePresenter.SchedulePresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.schedule_layout, container, false);

        ButterKnife.bind(this, v);

        ((BaseActivity)getActivity()).getSupportActionBar().setTitle(R.string.schedule);

        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.monday_view, R.id.tuesday_view, R.id.wednesday_view, R.id.thursday_view, R.id.friday_view, R.id.saturday_view, R.id.sunday_view})
    public void onClick(View view)
    {
        ScheduleDay day = null;

        switch (view.getId())
        {
            case R.id.monday_view:
                day = ScheduleDay.MONDAY;
                break;
            case R.id.tuesday_view:
                day = ScheduleDay.TUESDAY;
                break;
            case R.id.wednesday_view:
                day = ScheduleDay.WEDNESDAY;
                break;
            case R.id.thursday_view:
                day = ScheduleDay.THURSDAY;
                break;
            case R.id.friday_view:
                day = ScheduleDay.FRIDAY;
                break;
            case R.id.saturday_view:
                day = ScheduleDay.SATURDAY;
                break;
            case R.id.sunday_view:
                day = ScheduleDay.SUNDAY;
                break;
        }

        if (VersionUtils.isLollipopOrGreater())
        {
            presenter.dayClicked(day, view);
        }
        else
        {
            presenter.dayClicked(day);
        }
    }

    @Override
    public void startScheduleDayActivity(ScheduleDay scheduleDay)
    {
        Intent startingIntent = ScheduleDayActivity.getStartingIntent(getActivity(), scheduleDay);
        startActivity(startingIntent);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startScheduleDayActivity(ScheduleDay scheduleDay, View transitioningView)
    {
        String transitionName = transitioningView.getTransitionName();

        Intent startingIntent = ScheduleDayActivity.getStartingIntent(getActivity(), scheduleDay, transitionName);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitioningView, transitionName).toBundle();
        startActivity(startingIntent, options);
    }
}
