package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity.ActiveWorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity.WorkoutDetailsPresenter;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class LogWorkoutFragmentPresenter extends WorkoutSessionPresenter<LogWorkoutFragmentInterface>
{
    // TODO: Need to figure out where to unsubscribe from this...
    protected Subscription todaysWorkoutSubscription;

    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    public LogWorkoutFragmentPresenter()
    {
        super();
        fetchPopulatedWorkoutSession();
    }

    @Override
    public void onExerciseClicked(ExerciseSession session)
    {
        Intent intent = new Intent(attachedFragment.getActivity(), ActiveWorkoutDetailsActivity.class);
        intent.putExtra(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(session));
        fragmentInterface.startWorkoutDetailsActivity(intent, WORKOUT_DETAILS_REQUEST_CODE);
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

    protected void fetchPopulatedWorkoutSession()
    {
        todaysWorkoutSubscription = new WorkoutSessionDatabaseInteractor()
                .getTodaysWorkoutSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    workoutSession = Optional.absent();
                })
                .doOnCompleted(() -> {
                    if (!workoutSession.isPresent())
                    {
                        workoutSession = Optional.of(new WorkoutSession(DateUtils.getTodaysDate().getTime()));
                    }
                    fragmentInterface.updateWorkoutList(workoutSession.get());
                })
                .subscribe(session -> {
                    workoutSession = Optional.of(session);
                });
    }
}
