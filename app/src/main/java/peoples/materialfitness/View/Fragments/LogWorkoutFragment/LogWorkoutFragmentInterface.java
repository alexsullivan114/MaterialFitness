package peoples.materialfitness.View.Fragments.LogWorkoutFragment;

import peoples.materialfitness.View.Fragments.CoreFragment.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface LogWorkoutFragmentInterface extends BaseFragmentInterface
{
    void showAddWorkoutDialog();
    void errorExerciseTitleNull();
    void errorMuscleGroupTextNull();
    void dismissAddWorkoutDialog();
}
