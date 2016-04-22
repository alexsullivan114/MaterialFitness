package peoples.materialfitness.Model.WorkoutSession;

import rx.Observable;

/**
 * Created by Alex Sullivan on 4/22/2016.
 *
 * Simple caching class for todays workout session. This one is honestly less necessary that
 * the {@link CompleteWorkoutHistoryCache} cache, but its still one of the primary db calls
 * we'll make.
 */
public class TodaysWorkoutHistoryCache
{
    private static TodaysWorkoutHistoryCache INSTANCE;

    private static Observable<WorkoutSession> workoutSessionObservable;

    private TodaysWorkoutHistoryCache()
    {
        WorkoutSessionDatabaseInteractor interactor = new WorkoutSessionDatabaseInteractor();
        workoutSessionObservable = interactor
                .getTodaysWorkoutSession()
                .cache();
    }

    public static synchronized TodaysWorkoutHistoryCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new TodaysWorkoutHistoryCache();
        }

        return INSTANCE;
    }
    public Observable<WorkoutSession> getTodaysWorkoutObservable()
    {
        return workoutSessionObservable;
    }
}
