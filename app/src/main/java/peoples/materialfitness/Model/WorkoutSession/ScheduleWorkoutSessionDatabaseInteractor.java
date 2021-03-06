package peoples.materialfitness.Model.WorkoutSession;

import peoples.materialfitness.Model.FitnessDatabaseUtils;
import peoples.materialfitness.Model.ScheduleDay;
import rx.Observable;

/**
 * Created by Alex Sullivan on 5/15/2016.
 */
public class ScheduleWorkoutSessionDatabaseInteractor extends WorkoutSessionDatabaseInteractor
{
    @Override
    public Observable<WorkoutSession> fetchWithArguments(String whereClause, String[] args, String groupBy, String[] columns, String having, String orderBy, String limit)
    {
        return FitnessDatabaseUtils.getCursorObservable(WorkoutSessionContract.TABLE_NAME,
                                                        whereClause, args, groupBy, columns, having, orderBy, limit, context)
                .flatMap(this::getWorkoutSessionFromCursor);
    }

    @Override
    public Observable<WorkoutSession> fetchWithClause(String whereClause, String[] arguments)
    {
        return fetchWithArguments(whereClause, arguments, null, null, null, null, null);
    }

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
