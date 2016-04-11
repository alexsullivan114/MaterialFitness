package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class LogWorkoutFragmentPresenter extends WorkoutSessionPresenter<LogWorkoutFragmentInterface>
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    /**
     * Refresh our workout session if our data has been updated.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleWorkoutDetailsResults(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == WorkoutSessionPresenter.WORKOUT_DETAILS_REQUEST_CODE &&
                resultCode == WorkoutSessionPresenter.WORKOUT_DETAILS_CONTENT_UPDATED)
        {
            fetchPopulatedWorkoutSession();
        }
    }

    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }
}
