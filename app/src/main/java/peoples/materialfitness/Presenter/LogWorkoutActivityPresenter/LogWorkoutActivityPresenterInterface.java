package peoples.materialfitness.Presenter.LogWorkoutActivityPresenter;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.BaseActivityPresenterInterface;

/**
 * Created by Alex Sullivan on 10/24/15.
 */
public interface LogWorkoutActivityPresenterInterface extends BaseActivityPresenterInterface
{
    void createMuscleGroupChoiceDialog(List<String> muscleGroupTitles);
    void updateExerciseDialogForMuscleGroup(MuscleGroup muscleGroup);
    void exerciseCreated(Exercise exercise);
}
