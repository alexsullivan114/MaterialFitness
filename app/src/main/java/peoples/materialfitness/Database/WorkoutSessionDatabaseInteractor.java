package peoples.materialfitness.Database;

import java.util.Date;
import java.util.List;

import peoples.materialfitness.Util.DateUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutSessionDatabaseInteractor implements ModelDatabaseInteractor<WorkoutSession>
{
    @Override
    public Observable<WorkoutSession> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    @Override
    public Observable<WorkoutSession> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.empty();
//        Observable<WorkoutSession> sessionsObservable =
//                Observable.from(WorkoutSession.find(WorkoutSession.class, whereClause, arguments));
//
//        return sessionsObservable.flatMap(this::associateWorkoutSession);
    }

    public Observable<WorkoutSession> associateWorkoutSession(WorkoutSession session)
    {
        return Observable.empty();
//        final String WHERE_CLAUSE = ExerciseSession.WORKOUT_SESSION_ID_COLUMN + " = ?";
//        final String[] ARGS = new String[]{String.valueOf(session.getId())};
//        return new ExerciseSessionDatabaseInteractor().fetchWithClause(WHERE_CLAUSE, ARGS)
//                .toList()
//                .flatMap(exerciseSessions -> {
//                    session.setExercises(exerciseSessions);
//                    return Observable.just(session);
//                });

    }

    @Override
    public void save(final WorkoutSession entity)
    {
//        // Rxjava just makes threading so fun.
//        Observable.just(entity)
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    // Save the workout session.
//                    entity.save();
//                    // Save each exercise session associated with this workout session.
//                    ExerciseSessionDatabaseInteractor interactor = new ExerciseSessionDatabaseInteractor();
//                    for (ExerciseSession session : entity.getExercises())
//                    {
//                        session.setWorkoutSessionId(entity.getId());
//                        interactor.save(session);
//                    }
//                });
    }

    @Override
    public void delete(WorkoutSession entity)
    {
//        entity.delete();
    }

    public Observable<WorkoutSession> getTodaysWorkoutSession()
    {
        return getDatesWorkoutSession(DateUtils.getTodaysDate().getTime());
    }

    public Observable<WorkoutSession> getDatesWorkoutSession(long millisSinceEpoch)
    {
        return Observable.empty();
//        String whereClause = WorkoutSession.DATE_COLUMN + " = ?";
//        String[] arguments = new String[]{String.valueOf(millisSinceEpoch)};
//
//        return fetchWithClause(whereClause, arguments);
    }

    @Override
    public void cascadeSave(WorkoutSession entity)
    {

    }

    @Override
    public void cascadeDelete(WorkoutSession entity)
    {

    }
}
