package peoples.materialfitness.Schedule.ScheduleDay;

import java.util.Date;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.Cache.TodaysWorkoutDbCache;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.Model.WorkoutSession.ScheduleWorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
* Created by Alex Sullivan
*/
public class ScheduleDayPresenter extends BaseActivityPresenter<ScheduleDayInterface>
{
    private ScheduleDay scheduleDay;
    private WorkoutSession workoutSession;

    public static class ScheduleDayPresenterFactory implements PresenterFactory<ScheduleDayPresenter>
    {
        @Override
        public ScheduleDayPresenter createPresenter()
        {
            return new ScheduleDayPresenter();
        }
    }

    public ScheduleDayPresenter()
    {

    }

    public void setScheduleDay(ScheduleDay scheduleDay)
    {
        this.scheduleDay = scheduleDay;
        fetchScheduleDayWorkoutSession();
    }

    private void fetchScheduleDayWorkoutSession()
    {
        fetchWorkoutSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutSession -> {
                    this.workoutSession = workoutSession;

                    activityInterface.showFab();
                    activityInterface.displayWorkoutSession(workoutSession);
                });
    }

    void logAllClicked()
    {
        fetchWorkoutSession()
                .flatMap(session -> Observable.from(session.getExerciseSessions()))
                .doOnNext(exerciseSession -> {
                    ExerciseSession copy = new ExerciseSession();
                    copy.setExercise(exerciseSession.getExercise());
                    TodaysWorkoutDbCache.getInstance().pushExerciseSession(copy);
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private Observable<WorkoutSession> fetchWorkoutSession()
    {
        if (workoutSession != null)
        {
            return Observable.just(workoutSession);
        }
        else
        {
            return new ScheduleWorkoutSessionDatabaseInteractor()
                    .fetchWorkoutSessionForScheduleDay(scheduleDay);
        }
    }

    public Observable<WorkoutSession> getWorkoutSession()
    {
        return fetchWorkoutSession();
    }

    public void addExerciseClicked()
    {
        activityInterface.showAddExerciseDialog();
    }

    public void exerciseLogged(Exercise exercise)
    {
        // Check to see if this workout session already contains the exercise...
        if (!workoutSession.containsExercise(exercise))
        {
            // If not add the exercise.
            final ExerciseSession exerciseSession = new ExerciseSession(exercise, new Date().getTime());
            workoutSession.addExerciseSession(exerciseSession);
            // Update our UI
            activityInterface.updateExerciseCard(exerciseSession);
            // Fire off a save of the exercise. It won't do anything if we already have it.
            new ExerciseDatabaseInteractor().uniqueSaveExercise(exercise)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(savedExercise -> {
                        // Make sure our local exercise copy has the right ID.
                        exercise.setId(savedExercise.getId());
                        new WorkoutSessionDatabaseInteractor()
                                .cascadeSave(workoutSession)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe();

                    });
        }
    }

    public void itemDeleted(int position)
    {
        ExerciseSession exerciseSession = workoutSession.getExerciseSessions().get(position);
        workoutSession.getExerciseSessions().remove(position);

        new ExerciseSessionDatabaseInteractor()
                .delete(exerciseSession)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    activityInterface.removeExercise(position);
                });
    }
}