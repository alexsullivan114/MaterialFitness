package peoples.materialfitness.View.LogWorkoutActivity;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/26/2015.
 */
public interface LogWorkoutActivityInterface extends BaseActivityInterface
{
    void createMuscleGroupChoiceDialog(List<String> muscleGroupTitles);
    void updateExerciseDialogForMuscleGroup(MuscleGroup muscleGroup);
    void exerciseCreated(Exercise exercise);
}
