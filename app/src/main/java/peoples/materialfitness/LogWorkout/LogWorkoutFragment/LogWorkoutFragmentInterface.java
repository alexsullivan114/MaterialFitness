package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface LogWorkoutFragmentInterface extends BaseFragmentInterface
{
    void showAddWorkoutDialog();
    void updateExerciseCard(ExerciseSession exerciseSession);
    void updateWorkoutList(WorkoutSession workoutSession);
    void startWorkoutDetailsActivity(Intent startingIntent, int requestCode);
}
