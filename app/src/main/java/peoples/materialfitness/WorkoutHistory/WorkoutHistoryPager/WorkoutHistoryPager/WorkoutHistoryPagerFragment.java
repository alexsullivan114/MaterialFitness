package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryPager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationUtils;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.View.BaseFragment;
import peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryCalendarDialog.WorkoutHistoryCalendarDialogFragment;

/**
 * Created by Alex Sullivan on 12/24/15.
 */
public class WorkoutHistoryPagerFragment extends BaseFragment<WorkoutHistoryPagerFragmentPresenter>
        implements WorkoutHistoryPagerFragmentInterface,
                   ViewPager.OnPageChangeListener,
                   DatePickerDialog.OnDateSetListener
{
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private Optional<String> titleString = Optional.absent();

    public static WorkoutHistoryPagerFragment newInstance()
    {
        WorkoutHistoryPagerFragment fragment = new WorkoutHistoryPagerFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected PresenterFactory<WorkoutHistoryPagerFragmentPresenter> getPresenterFactory()
    {
        return new WorkoutHistoryPagerFragmentPresenter.WorkoutHistoryFragmentPresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_workout_history, container, false);
        ButterKnife.bind(this, v);

        pager.setAdapter(new WorkoutHistoryPagerAdapter(getFragmentManager(), presenter.getWorkoutSessions()));
        pager.addOnPageChangeListener(this);

        ((BaseActivity)getActivity()).getSupportActionBar().setTitle(R.string.workout_history_title);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(presenter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(presenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(R.string.calendar_menu_item_tooltip)
                .setIcon(R.drawable.ic_date_range_white_24dp)
                .setOnMenuItemClickListener(item -> {
                    presenter.calendarMenuClicked();
                    return true;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setWorkoutSessions(List<WorkoutSession> workoutSessions)
    {
        if (pager != null)
        {
            pager.setAdapter(new WorkoutHistoryPagerAdapter(getFragmentManager(), workoutSessions));
            AnimationUtils.fadeVisibilityChange(pager, View.VISIBLE);
            AnimationUtils.fadeVisibilityChange(progressBar, View.GONE);
        }
    }

    @Override
    public void setTitle(String title)
    {
        if (getActivity() != null)
        {
            ((BaseActivity)getActivity()).getSupportActionBar().setTitle(title);
        }
        else
        {
            titleString = Optional.of(title);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (titleString.isPresent())
        {
            ((BaseActivity)getActivity()).getSupportActionBar().setTitle(titleString.get());
            titleString = Optional.absent();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        // no-op for now
    }

    @Override
    public void onPageSelected(int position)
    {
        presenter.pageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // no-op for now.
    }

    @Override
    public void openDatePickerDialog()
    {
        WorkoutHistoryCalendarDialogFragment.newInstance(presenter.getWorkoutSessions()).show(getFragmentManager(), TAG);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {

    }

    @Override
    public void hideFab()
    {
        ((RootFabDisplay) getActivity()).hideFab();
    }

    @Override
    public void scrollToIndex(int index)
    {
        pager.setCurrentItem(index, true);
    }
}
