package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager.WorkoutHistory;

import android.content.Intent;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.WorkoutDetails.HistoricalWorkoutDetailsActivity.HistoricalWorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsPresenter;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class WorkoutHistoryFragmentPresenter extends WorkoutSessionPresenter<WorkoutHistoryFragmentInterface>
{
    public static class WorkoutHistoryFragmentPresenterFactory implements PresenterFactory<WorkoutHistoryFragmentPresenter>
    {
        @Override
        public WorkoutHistoryFragmentPresenter createPresenter()
        {
            return new WorkoutHistoryFragmentPresenter();
        }
    }

    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        mWorkoutSession = Optional.of(workoutSession);
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        Intent intent = new Intent(attachedFragment.getActivity(), HistoricalWorkoutDetailsActivity.class);
        intent.putExtra(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(session));
        fragmentInterface.startWorkoutDetailsActivity(intent, WORKOUT_DETAILS_REQUEST_CODE);
    }
}
