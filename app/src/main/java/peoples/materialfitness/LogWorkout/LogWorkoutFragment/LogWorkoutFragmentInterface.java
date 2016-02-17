package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface LogWorkoutFragmentInterface extends BaseFragmentInterface
{
    void showAddWorkoutDialog();
    void updateExerciseCard(ExerciseSession exerciseSession);
    void updateWorkoutList(WorkoutSession workoutSession);
}
