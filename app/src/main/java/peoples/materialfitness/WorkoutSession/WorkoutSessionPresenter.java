package peoples.materialfitness.WorkoutSession;

import com.google.common.base.Optional;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public abstract class WorkoutSessionPresenter<T extends WorkoutSessionFragmentInterface> extends BaseFragmentPresenter<T>
{
    protected  WorkoutSession workoutSession;

    protected static final int WORKOUT_DETAILS_REQUEST_CODE = 12312;
    public static final int WORKOUT_DETAILS_CONTENT_UPDATED = 124412;

    public abstract void onExerciseClicked(ExerciseSession exerciseSession);

    public Optional<WorkoutSession> getWorkoutSession()
    {
        return Optional.fromNullable(workoutSession);
    }
}
