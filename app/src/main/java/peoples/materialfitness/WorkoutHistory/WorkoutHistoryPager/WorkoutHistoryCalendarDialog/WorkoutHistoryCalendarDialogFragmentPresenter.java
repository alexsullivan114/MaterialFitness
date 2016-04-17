package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistoryCalendarDialog;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by Alex Sullivan on 4/16/2016.
 */
public class WorkoutHistoryCalendarDialogFragmentPresenter extends BaseFragmentPresenter<WorkoutHistoryCalendarDialogFragmentInterface>
{
    private Optional<List<WorkoutSession>> workoutSessions = Optional.absent();

    public WorkoutHistoryCalendarDialogFragmentPresenter(WorkoutHistoryCalendarDialogFragmentInterface fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }

    @NonNull
    public Set<CalendarDay> getMarkedDays()
    {
        Set<CalendarDay> markedDays = new HashSet<>();

        if (workoutSessions.isPresent())
        {
            Calendar calendar = GregorianCalendar.getInstance();

            for (WorkoutSession workoutSession : workoutSessions.get())
            {
                calendar.setTimeInMillis(workoutSession.getWorkoutSessionDate());
                CalendarDay calendarDay = CalendarDay.from(calendar);
                markedDays.add(calendarDay);
            }
        }

        return markedDays;
    }

    public void setWorkoutSessions(@NonNull List<WorkoutSession> workoutSessions)
    {
        this.workoutSessions = Optional.of(workoutSessions);
    }

    public void onDateSelected(CalendarDay calendarDay)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date d = calendarDay.getDate();
        Calendar calendar = Calendar.getInstance();
        for (WorkoutSession workoutSession : workoutSessions.get())
        {
            calendar.setTimeInMillis(workoutSession.getWorkoutSessionDate());
            Date workoutSessionDate = calendar.getTime();

            if (formatter.format(d).equals(formatter.format(workoutSessionDate)))
            {
                fragmentInterface.postWorkoutSession(workoutSession);
                fragmentInterface.dismissCalendar();
            }
        }
    }
}
