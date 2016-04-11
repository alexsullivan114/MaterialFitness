package peoples.materialfitness.WorkoutDetails;

import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.View.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public interface WorkoutDetailsActivityInterface extends BaseActivityInterface
{
    void setTitle(String title);
    void showAddSetDialog(int reps, int weight);
    void addSet(WeightSet set);
    void contentUpdated(boolean didUpdate);
    void showBottomFab();
    void hideBottomFab();
}
