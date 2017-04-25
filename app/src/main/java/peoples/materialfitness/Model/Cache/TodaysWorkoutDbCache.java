package peoples.materialfitness.Model.Cache;

import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.Util.ExerciseSessionUtils;
import peoples.materialfitness.Util.WorkoutSessionUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by Alex Sullivan on 10/16/2016.
 * <p>
 * TODO: I dont think this cache should save values to the database, the database should push
 * updates to this cache.
 * <p>
 * TODO: I think this cache needs to be more reactive in nature - it should expose an observable
 * for a workout session that gets updates pushed to it.
 */

public class TodaysWorkoutDbCache implements TodaysWorkoutCache
{
    private static final String TAG = TodaysWorkoutDbCache.class.getSimpleName();

    private static TodaysWorkoutDbCache INSTANCE;
    private BehaviorSubject<WorkoutSession> todaysWorkoutSession = BehaviorSubject.create();

    private TodaysWorkoutDbCache()
    {
        initializeTodaysWorkoutSession();
    }

    public static TodaysWorkoutDbCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new TodaysWorkoutDbCache();
        }

        return INSTANCE;
    }

    @Override
    public Observable<WorkoutSession> todaysWorkoutObservable()
    {
        return todaysWorkoutSession.asObservable();
    }

    @Override
    public void pushWeightSet(WeightSet weightSet)
    {
        applyWorkoutSessionModification(workoutSession -> {
            for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
            {
                if (exerciseSession.getId() == weightSet.getExerciseSessionId())
                {
                    exerciseSession.addSet(weightSet);
                    new WeightSetDatabaseInteractor().saveWithPrUpdates(weightSet, exerciseSession.getExercise()).subscribe();
                    todaysWorkoutSession.onNext(workoutSession);
                    break;
                }
            }
        });
    }

    @Override
    public void pushExerciseSession(ExerciseSession exerciseSession)
    {
        applyWorkoutSessionModification(workoutSession -> {
            if (!workoutSession.containsExercise(exerciseSession.getExercise()))
            {
                new ExerciseDatabaseInteractor().uniqueSaveExercise(exerciseSession.getExercise())
                        .subscribe(exercise -> {
                            exerciseSession.setExercise(exercise);
                            exerciseSession.setWorkoutSessionId(workoutSession.getId());
                            new ExerciseSessionDatabaseInteractor().save(exerciseSession).subscribe();
                            workoutSession.addExerciseSession(exerciseSession);
                            todaysWorkoutSession.onNext(workoutSession);
                        });
            }
        });
    }

    @Override
    public void deleteWeightSet(WeightSet weightSet)
    {
        applyWorkoutSessionModification(workoutSession -> {
            final ExerciseSession exerciseSession =
                    ExerciseSessionUtils.getExerciseSessionForSet(weightSet, workoutSession.getExerciseSessions());
            if (exerciseSession != null)
            {
                ExerciseSessionUtils.deleteSetIfPresent(weightSet, exerciseSession);
                new WeightSetDatabaseInteractor().deleteWithPrUpdates(weightSet, exerciseSession.getExercise()).subscribe();
            }
            else
            {
                // Not sure why we'd ever be here, but handle it.
                new WeightSetDatabaseInteractor().delete(weightSet).subscribe();
            }
            todaysWorkoutSession.onNext(workoutSession);
        });
    }

    @Override
    public void deleteExerciseSession(ExerciseSession exerciseSession)
    {
        applyWorkoutSessionModification(workoutSession -> {
            WorkoutSessionUtils.removeExerciseSessionIfPresent(exerciseSession, workoutSession);
            new ExerciseSessionDatabaseInteractor().cascadeDelete(exerciseSession).subscribe();
            todaysWorkoutSession.onNext(workoutSession);
        });
    }

    @Override
    public void editWeightSet(WeightSet weightSet)
    {
        applyWorkoutSessionModification(workoutSession -> {
            final ExerciseSession session = ExerciseSessionUtils.
                    getExerciseSessionForSet(weightSet, workoutSession.getExerciseSessions());
            final WeightSet existingSet = ExerciseSessionUtils.getWeightSet(weightSet.getId(), session);
            existingSet.setNumReps(weightSet.getNumReps());
            existingSet.setWeight(weightSet.getWeight());
            new WeightSetDatabaseInteractor().editWithPrUpdates(existingSet, session.getExercise()).subscribe();
            todaysWorkoutSession.onNext(workoutSession);
        });
    }

    private void applyWorkoutSessionModification(Action1<WorkoutSession> action)
    {
        todaysWorkoutSession
                .subscribeOn(Schedulers.io())
                .take(1)
                .subscribe(action);
    }

    private void initializeTodaysWorkoutSession()
    {
        new WorkoutSessionDatabaseInteractor()
                .getTodaysWorkoutSession()
                .subscribeOn(Schedulers.io())
                .switchIfEmpty(createTodaysWorkoutSession())
                .subscribe(workoutSession -> {
                    todaysWorkoutSession.onNext(workoutSession);
                });
    }

    private Observable<WorkoutSession> createTodaysWorkoutSession()
    {
        WorkoutSession workoutSession = new WorkoutSession(DateUtils.getTodaysDate().getTime());
        return new WorkoutSessionDatabaseInteractor().save(workoutSession);
    }

//    public void addExerciseSessionToCache(final boolean saveUpdate,
//                                          final ExerciseSession exerciseSession)
//    {
//
//        getTodaysWorkoutSession()
//                .observeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    workoutSession.uniqueAddExerciseSession(exerciseSession);
//                    if (saveUpdate)
//                    {
//                        new ExerciseSessionDatabaseInteractor().cascadeSave(exerciseSession).subscribe();
//                    }
//                });
//    }
//
//    public void addSets(final boolean saveUpdate,
//                        final List<WeightSet> newSets)
//    {
//        getTodaysWorkoutSession()
//                .observeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    final WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();
//                    for (WeightSet set : newSets)
//                    {
//                        for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
//                        {
//                            if (exerciseSession.getId() == set.getExerciseSessionId())
//                            {
//                                exerciseSession.addSet(set);
//
//                                if (saveUpdate)
//                                {
//                                    interactor.saveWithPrUpdates(set, exerciseSession.getExercise()).subscribe();
//                                }
//                            }
//                        }
//                    }
//                }, (throwable -> {
//                    Log.e(TAG, throwable.toString());
//                }));
//    }
//
//    public void deleteExerciseSession(final boolean saveUpdate,
//                                      final ExerciseSession exerciseSession)
//    {
//        getTodaysWorkoutSession()
//                .observeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    if (saveUpdate)
//                    {
//                        new ExerciseSessionDatabaseInteractor().delete(exerciseSession).subscribe();
//                    }
//                });
//    }
//
//    public void deleteSet(final boolean saveUpdate,
//                          final WeightSet set)
//    {
//        getTodaysWorkoutSession()
//                .observeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//                    for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
//                    {
//                        if (exerciseSession.getId() == set.getExerciseSessionId())
//                        {
//                            exerciseSession.getSets().remove(set);
//                            if (saveUpdate)
//                            {
//                                new WeightSetDatabaseInteractor().deleteWithPrUpdates(set, exerciseSession.getExercise()).subscribe();
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void editSet(final boolean saveUpdate,
//                        final WeightSet set)
//    {
//        getTodaysWorkoutSession()
//                .observeOn(Schedulers.io())
//                .subscribe(workoutSession -> {
//
//                    for (int i = 0; i < workoutSession.getExerciseSessions().size(); i++)
//                    {
//                        final ExerciseSession session = workoutSession.getExerciseSessions().get(i);
//                        if (session.getId() == set.getExerciseSessionId())
//                        {
//                            final WeightSet updateSet = session.getSets().get(i);
//                            updateSet.setNumReps(set.getNumReps());
//                            updateSet.setWeight(set.getWeight());
//                            if (saveUpdate)
//                            {
//                                new WeightSetDatabaseInteractor().editWithPrUpdates(set, session.getExercise()).subscribe();
//                            }
//                        }
//                    }
//                });
//    }
}
