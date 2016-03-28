package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;

import org.parceler.Parcels;

import java.util.List;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsPresenter extends BaseActivityPresenter<WorkoutDetailsActivityInterface>
{
    public ExerciseSession mExerciseSession;

    public static final String EXTRA_EXERCISE_SESSION = "extraExercise";

    public static class WorkoutDetailsPresenterFactory implements PresenterFactory<WorkoutDetailsPresenter>
    {
        @Override
        public WorkoutDetailsPresenter createPresenter()
        {
            return new WorkoutDetailsPresenter();
        }
    }

    @Override
    public void setBundle(Bundle bundle)
    {
        super.setBundle(bundle);

        if (bundle != null && bundle.containsKey(EXTRA_EXERCISE_SESSION))
        {
            mExerciseSession = Parcels.unwrap(bundle.getParcelable(EXTRA_EXERCISE_SESSION));
            activityInterface.setTitle(mExerciseSession.getExercise().getTitle());
        }
    }

    public void fabClicked()
    {
        String repsText = "";
        String weightText = "";

        List<WeightSet> addedSets = mExerciseSession.getSets();

        if (addedSets.size() >= 1)
        {
            WeightSet lastWeightSet = addedSets.get(addedSets.size() - 1);
            repsText = String.valueOf(lastWeightSet.getNumReps());
            weightText = String.valueOf(lastWeightSet.getWeight());
        }

        activityInterface.showAddSetDialog(repsText, weightText);
    }

    public void addSet(int reps, int weight)
    {
        WeightSet set = new WeightSet(weight, reps);
        set.setExerciseSessionId(mExerciseSession.getId());
        new WeightSetDatabaseInteractor().save(set).subscribe();
        mExerciseSession.addSet(set);
        activityInterface.addSet(set);
        activityInterface.contentUpdated(true);
    }
}
