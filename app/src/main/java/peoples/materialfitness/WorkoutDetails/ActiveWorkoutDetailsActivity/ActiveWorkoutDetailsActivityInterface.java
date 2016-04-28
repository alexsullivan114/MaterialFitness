package peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity;

import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivityInterface;

/**
 * Created by Alex Sullivan on 4/18/2016.
 */
public interface ActiveWorkoutDetailsActivityInterface extends WorkoutDetailsActivityInterface
{
    void showAddSetDialog(int reps, int weight);
    void addSet(WeightSet set);
    void showBottomFab();
    void hideBottomFab();
    void completed();
    void showDeleteConfirmationView();
}
