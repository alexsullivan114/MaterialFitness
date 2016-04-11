package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager;

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

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.View.BaseFragment;

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

        v.post(this::onViewVisible);

        return v;
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

    private void onViewVisible()
    {
        ((RootFabDisplay) getActivity()).hideFab();
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
        }
    }

    @Override
    public void setTitle(String title)
    {
        ((BaseActivity)getActivity()).getSupportActionBar().setTitle(title);
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
        new DatePickerDialog(getActivity(), this, 1990, 11, 4).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {

    }
}
