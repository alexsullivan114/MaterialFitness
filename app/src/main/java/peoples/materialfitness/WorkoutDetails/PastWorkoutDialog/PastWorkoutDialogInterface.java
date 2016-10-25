package peoples.materialfitness.WorkoutDetails.PastWorkoutDialog;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.View.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 5/4/2016.
 */
public interface PastWorkoutDialogInterface extends BaseActivityInterface
{
    void setupRecyclerView(ExerciseSession exerciseSession);
    void setWeightSetAsPr(WeightSet weightSet);
}
