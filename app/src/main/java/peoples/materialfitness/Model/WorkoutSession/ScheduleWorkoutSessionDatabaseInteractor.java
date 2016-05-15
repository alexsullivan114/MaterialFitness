package peoples.materialfitness.Model.WorkoutSession;

import java.util.List;

import peoples.materialfitness.Schedule.ScheduleDay;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alex Sullivan on 5/15/2016.
 */
public class ScheduleWorkoutSessionDatabaseInteractor extends WorkoutSessionDatabaseInteractor
{
    public Observable<WorkoutSession> fetchWorkoutSessionForScheduleDay(ScheduleDay scheduleDay)
    {
        long workoutSessionId = scheduleDay.getWorkoutSessionId();

        String WHERE_CLAUSE = WorkoutSessionContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(workoutSessionId)};
        return fetchWithClause(WHERE_CLAUSE, ARGS)
                .toList()
                .flatMap(workoutSessions -> {
                    if (workoutSessions.size() == 0)
                    {
                        return save(createScheduleWorkoutSession(scheduleDay));
                    }
                    else
                    {
                        return Observable.from(workoutSessions);
                    }
                });
    }

    private WorkoutSession createScheduleWorkoutSession(ScheduleDay scheduleDay)
    {
        WorkoutSession workoutSession = new WorkoutSession(0);
        workoutSession.setId(scheduleDay.getWorkoutSessionId());
        return workoutSession;
    }
}
