package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;

import org.parceler.Parcels;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WorkoutSession;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsPresenter;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
    implements LogWorkoutDialog.OnExerciseLoggedCallback
{
    public WorkoutSession mWorkoutSession;

    public LogWorkoutFragmentPresenter()
    {
        // TODO: Load this from the database to see if we have one for today.
        mWorkoutSession = new WorkoutSession();
    }

    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }

    public void onExerciseClicked(ExerciseSession session)
    {
        Intent intent = new Intent(attachedFragment.getActivity(), WorkoutDetailsActivity.class);
        intent.putExtra(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(session));
        attachedFragment.startActivity(intent);
    }

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        // Fire off a save of the exercise. It won't do anything if we already have it.
        new ExerciseDatabaseInteractor().uniqueSaveExercise(exercise);

        // Check to see if this workout session already contains the exercise...
        if (!mWorkoutSession.containsExercise(exercise))
        {
            // If not add the exercise.
            ExerciseSession exerciseSession = new ExerciseSession(exercise);
            mWorkoutSession.addExerciseSession(exerciseSession);
            fragmentInterface.updateExerciseCard(exerciseSession);
        }
    }
}
