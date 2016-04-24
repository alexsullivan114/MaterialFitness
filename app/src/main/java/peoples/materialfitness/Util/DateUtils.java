package peoples.materialfitness.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Alex Sullivan on 2/16/16.
 */
public class DateUtils
{
    public static Date getTodaysDate()
    {
        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date.getTime();
    }

    public static boolean isToday(Date date)
    {
        return isToday(date.getTime());
    }

    public static boolean isToday(long millisSinceEpoch)
    {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millisSinceEpoch);

        return (today.get(Calendar.ERA) == date.get(Calendar.ERA) &&
                today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR));
    }

    public static long getDatesMidnightTime(long millisSinceEpoch)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisSinceEpoch);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getDatesEndOfDayTime(long millisSinceEpoch)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisSinceEpoch);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * Returns a date string of the format MM/DD/YYYY
     * @param millisSinceEpoch
     * @return
     */
    public static String getShortDateDisplayString(long millisSinceEpoch)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisSinceEpoch);

        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(calendar.getTime());
    }
}
