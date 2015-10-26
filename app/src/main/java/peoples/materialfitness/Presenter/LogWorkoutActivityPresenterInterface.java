package peoples.materialfitness.Presenter;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.MuscleGroup;

/**
 * Created by Alex Sullivan on 10/24/15.
 */
public interface LogWorkoutActivityPresenterInterface extends BaseActivityPresenterInterface
{
    void createMuscleGroupChoiceDialog(List<String> muscleGroupTitles);
    void updateExerciseDialogForMuscleGroup(MuscleGroup muscleGroup);
    void exerciseCreated(Exercise exercise);
}
