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
import android.widget.TextView;

import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.AnimationUtils;
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
    @Bind(R.id.workouts_will_appear_textview)
    TextView emptyTextView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_workout_history, container, false);
        ButterKnife.bind(this, v);

        setWorkoutSessions(presenter.getWorkoutSessions());
        pager.setCurrentItem(presenter.getCurrentPosition());
        if (presenter.getCurrentDateString().isPresent())
        {
            setTitle(presenter.getCurrentDateString().get());
        }
        else
        {
            setTitle(getResources().getString(R.string.workout_history_title));
        }

        pager.addOnPageChangeListener(this);

        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(presenter);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(presenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
            setupAdapter(workoutSessions);
            if (workoutSessions.size() > 0)
            {
                AnimationUtils.fadeVisibilityChange(pager, View.VISIBLE);
                AnimationUtils.fadeVisibilityChange(progressBar, View.GONE);
            }
            else
            {
                AnimationUtils.fadeVisibilityChange(progressBar, View.GONE);
                AnimationUtils.fadeVisibilityChange(emptyTextView, View.VISIBLE);
            }
        }
    }

    private void setupAdapter(List<WorkoutSession> workoutSessions)
    {
        pager.setAdapter(new WorkoutHistoryPagerAdapter(getChildFragmentManager(), workoutSessions));
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
