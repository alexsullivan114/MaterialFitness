package peoples.materialfitness.Schedule.ScheduleDay;

import java.util.Date;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ScheduleDay;
import peoples.materialfitness.Model.WorkoutSession.ScheduleWorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
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
        new ScheduleWorkoutSessionDatabaseInteractor()
                .fetchWorkoutSessionForScheduleDay(scheduleDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutSession -> {
                    this.workoutSession = workoutSession;

                    activityInterface.showFab();
                    activityInterface.setWorkoutSession(workoutSession);
                    if (workoutSession.getExercises().size() > 0)
                    {
                        activityInterface.displayWorkoutSession(workoutSession);
                    }
                    else
                    {
                        activityInterface.showEmptyScreen();
                    }
                });
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
}