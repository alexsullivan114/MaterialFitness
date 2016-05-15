package peoples.materialfitness.Schedule;

/**
 * Created by Alex Sullivan on 5/15/2016.
 */
public enum ScheduleDay
{
    MONDAY(-10),
    TUESDAY(-11),
    WEDNESDAY(-12),
    THURSDAY(-13),
    FRIDAY(-14),
    SATURDAY(-15),
    SUNDAY(-16);

    private final long workoutSessionId;

    private ScheduleDay(long workoutSessionId)
    {
        this.workoutSessionId = workoutSessionId;
    }

    public long getWorkoutSessionId()
    {
        return workoutSessionId;
    }
}
