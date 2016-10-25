package peoples.materialfitness.Model.Cache;

import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.Observable;

/**
 * Created by Alex Sullivan on 4/22/2016.
 *
 * Simple caching class for all workout sessions. This operation becomes lengthy; let's cache the
 * results to make it faster! The obvious drawback is that we're keeping all these workout sessions
 * in memory. That's not ideal, but I believe the cost is worth it.
 */
public class CompleteWorkoutHistoryCache
{
    private static CompleteWorkoutHistoryCache INSTANCE;

    private static Observable<WorkoutSession> workoutSessionObservable;

    private CompleteWorkoutHistoryCache()
    {
        final String ordering = WorkoutSessionContract.COLUMN_NAME_DATE + " " + ModelDatabaseInteractor.Ordering.DESC.name();

        WorkoutSessionDatabaseInteractor interactor = new WorkoutSessionDatabaseInteractor();
        workoutSessionObservable = interactor.fetchAll(null, ordering)
                .cache()
                .onBackpressureBuffer();
    }

    public static synchronized CompleteWorkoutHistoryCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CompleteWorkoutHistoryCache();
        }

        return INSTANCE;
    }
    public Observable<WorkoutSession> getAllWorkoutSessions()
    {
        return workoutSessionObservable;
    }
}
