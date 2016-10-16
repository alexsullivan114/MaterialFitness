package peoples.materialfitness.Model.WorkoutSession;

import java.util.List;

import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Alex Sullivan on 10/16/2016.
 */

public class TodaysWorkoutHistoryCache
{
    private static TodaysWorkoutHistoryCache INSTANCE;
    private WorkoutSession todaysWorkoutSession;

    private TodaysWorkoutHistoryCache() {}

    public static TodaysWorkoutHistoryCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new TodaysWorkoutHistoryCache();
        }

        return INSTANCE;
    }

    public Observable<WorkoutSession> getTodaysWorkoutSession()
    {
        if (todaysWorkoutSession == null)
        {
            return createTodaysWorkoutSession();
        }
        else
        {
            return Observable.just(todaysWorkoutSession);
        }
    }

    private Observable<WorkoutSession> createTodaysWorkoutSession()
    {
        return new WorkoutSessionDatabaseInteractor()
                .getTodaysWorkoutSession()
                .toList()
                .flatMap(workoutSessions -> {
                   if (workoutSessions.size() == 0)
                   {
                       WorkoutSession workoutSession = new WorkoutSession(DateUtils.getTodaysDate().getTime());
                       return new WorkoutSessionDatabaseInteractor().save(workoutSession);
                   }
                   else
                   {
                       return Observable.from(workoutSessions);
                   }
                })
                .doOnNext(workoutSession -> todaysWorkoutSession = workoutSession);
    }

    public void updateCachedWorkoutSession(final boolean saveUpdate,
                                           final WorkoutSession updatedSession)
    {
        todaysWorkoutSession = updatedSession;

        if (saveUpdate)
        {
            new WorkoutSessionDatabaseInteractor().cascadeSave(todaysWorkoutSession);
        }
    }
}
