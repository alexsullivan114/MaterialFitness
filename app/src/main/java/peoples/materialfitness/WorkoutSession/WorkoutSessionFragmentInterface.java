package peoples.materialfitness.WorkoutSession;

import android.content.Intent;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface WorkoutSessionFragmentInterface extends BaseFragmentInterface
{
    void updateExerciseCard(ExerciseSession exerciseSession);
    void updateWorkoutList(WorkoutSession workoutSession);
    void startWorkoutDetailsActivity(Intent startingIntent, int requestCode);
    void addPr(WeightSet prSet, Exercise exercise);
}
