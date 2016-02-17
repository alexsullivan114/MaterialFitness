package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;

import org.parceler.Parcels;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Database.ExerciseSession;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsPresenter extends BaseActivityPresenter<WorkoutDetailsActivityInterface>
{
    private ExerciseSession mExerciseSession;
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
        activityInterface.showAddRepDialog();
    }
}
