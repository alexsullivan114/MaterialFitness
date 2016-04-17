package peoples.materialfitness.View.Components.MultiDayCalendarDialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 4/16/2016.
 */
public abstract class MultiDayCalendarDialog extends DialogFragment
{
    @Bind(R.id.calendarView)
    protected MaterialCalendarView calendarView;

    @Override
    public void onStart()
    {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @NonNull
    public abstract Set<CalendarDay> getMarkedDays();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.multi_day_calendar_view, container, false);
        ButterKnife.bind(this, v);

        calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.colorPrimaryDark), getMarkedDays()));

        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private static class EventDecorator implements DayViewDecorator
    {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates)
        {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day)
        {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view)
        {
            view.addSpan(new DotSpan(15, color));
        }
    }
}
