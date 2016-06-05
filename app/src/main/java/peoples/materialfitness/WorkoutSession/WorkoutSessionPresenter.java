package peoples.materialfitness.WorkoutSession;

import com.google.common.base.Optional;

import java.util.Date;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.AddExerciseDialog;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public abstract class WorkoutSessionPresenter<T extends WorkoutSessionFragmentInterface> extends BaseFragmentPresenter<T>
    implements AddExerciseDialog.OnExerciseLoggedCallback
{
    protected Optional<WorkoutSession> workoutSession = Optional.absent();

    public static final int WORKOUT_DETAILS_REQUEST_CODE = 12312;
    public static final int WORKOUT_DETAILS_CONTENT_UPDATED = 124412;

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        // Check to see if this workout session already contains the exercise...
        if (!workoutSession.get().containsExercise(exercise))
        {
            // If not add the exercise.
            final ExerciseSession exerciseSession = new ExerciseSession(exercise, new Date().getTime());
            workoutSession.get().addExerciseSession(exerciseSession);
            // Update our UI
            fragmentInterface.updateExerciseCard(exerciseSession);
            // Fire off a save of the exercise. It won't do anything if we already have it.
            new ExerciseDatabaseInteractor().uniqueSaveExercise(exercise)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(savedExercise -> {
                        // Make sure our local exercise copy has the right ID.
                        exercise.setId(savedExercise.getId());
                        new WorkoutSessionDatabaseInteractor()
                                .cascadeSave(workoutSession.get())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe();

                    });
        }
    }

    public abstract void onExerciseClicked(ExerciseSession exerciseSession);

    public Optional<WorkoutSession> getWorkoutSession()
    {
        return workoutSession;
    }
}
