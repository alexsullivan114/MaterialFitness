package peoples.materialfitness.Model.WorkoutSession;

import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ScheduleDay;
import rx.Observable;

/**
 * Created by Alex Sullivan on 5/15/2016.
 *
 * This database interactor does not extend our base database interactor because the only
 * valid fetch is to get the workout session for a given schedule day. None of the other DB
 * operations apply.
 */
public class ScheduleWorkoutSessionDatabaseInteractor
{
    public Observable<WorkoutSession> fetchWorkoutSessionForScheduleDay(ScheduleDay scheduleDay)
    {
        long workoutSessionId = scheduleDay.getWorkoutSessionId();

        String WHERE_CLAUSE = WorkoutSessionContract._ID + " = ?";
        String[] ARGS = new String[]{String.valueOf(workoutSessionId)};
        WorkoutSessionDatabaseInteractor interactor = new WorkoutSessionDatabaseInteractor();
        return interactor.fetchWithClause(WHERE_CLAUSE, ARGS)
                .toList()
                .flatMap(workoutSessions -> {
                    if (workoutSessions.size() == 0)
                    {
                        return interactor.save(createScheduleWorkoutSession(scheduleDay));
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
