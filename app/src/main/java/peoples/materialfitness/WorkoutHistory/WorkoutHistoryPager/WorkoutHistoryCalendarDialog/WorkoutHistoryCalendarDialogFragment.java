package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryCalendarDialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.greenrobot.eventbus.*;
import org.parceler.Parcels;

import java.util.List;
import java.util.Set;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.Components.MultiDayCalendarDialog.MultiDayCalendarDialog;

/**
 * Created by Alex Sullivan on 4/16/2016.
 */
public class WorkoutHistoryCalendarDialogFragment extends MultiDayCalendarDialog
        implements WorkoutHistoryCalendarDialogFragmentInterface,
                   OnDateSelectedListener
{
    private static final String WORKOUTS_KEY = "workoutsKey";
    private WorkoutHistoryCalendarDialogFragmentPresenter presenter;

    public static WorkoutHistoryCalendarDialogFragment newInstance(List<WorkoutSession> workoutSessions)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKOUTS_KEY, Parcels.wrap(workoutSessions));

        WorkoutHistoryCalendarDialogFragment fragment = new WorkoutHistoryCalendarDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        presenter = new WorkoutHistoryCalendarDialogFragmentPresenter(this);
        List<WorkoutSession> workoutSessions = Parcels.unwrap(getArguments().getParcelable(WORKOUTS_KEY));
        presenter.setWorkoutSessions(workoutSessions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        calendarView.setOnDateChangedListener(this);
        return v;
    }

    @Override
    @NonNull
    public Set<CalendarDay> getMarkedDays()
    {
        return presenter.getMarkedDays();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
    {
        presenter.onDateSelected(date);
    }

    @Override
    public void postWorkoutSession(WorkoutSession workoutSession)
    {
        EventBus.getDefault().post(new WorkoutCalendarSessionSelected(workoutSession));
    }

    @Override
    public void dismissCalendar()
    {
        dismiss();
    }

    public static final class WorkoutCalendarSessionSelected
    {
        public WorkoutSession workoutSession;

        public WorkoutCalendarSessionSelected(WorkoutSession workoutSession)
        {
            this.workoutSession = workoutSession;
        }
    }
}
