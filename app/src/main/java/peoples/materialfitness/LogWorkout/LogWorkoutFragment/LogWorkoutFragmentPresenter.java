package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Intent;
import android.util.Log;

import org.parceler.Parcels;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.AddExerciseDialog;
import peoples.materialfitness.Model.Cache.DatabasePrCache;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.Cache.TodaysWorkoutHistoryCache;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity.ActiveWorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity.WorkoutDetailsPresenter;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 4/11/2016.
 *
 * TODO: The workout session in this should just be a stream from the todays workout session cache.
 */
class LogWorkoutFragmentPresenter extends WorkoutSessionPresenter<LogWorkoutFragmentInterface>
        implements AddExerciseDialog.OnExerciseLoggedCallback
{
    // TODO: Need to figure out where to unsubscribe from this...
    private Subscription todaysWorkoutSubscription;

    static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    private LogWorkoutFragmentPresenter()
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

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        // Check to see if this workout session already contains the exercise...
        if (!workoutSession.containsExercise(exercise))
        {
            // If not add the exercise.
            final ExerciseSession exerciseSession = new ExerciseSession(exercise, workoutSession.getId());
            workoutSession.addExerciseSession(exerciseSession);
            // Update our UI
            TodaysWorkoutHistoryCache.getInstance()
                    .addExerciseSessionToCache(true, exerciseSession);
            fragmentInterface.updateExerciseCard(exerciseSession);
        }
    }

    /**
     * Refresh our workout session if our data has been updated.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void handleWorkoutDetailsResults(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == WorkoutSessionPresenter.WORKOUT_DETAILS_REQUEST_CODE &&
                resultCode == WorkoutSessionPresenter.WORKOUT_DETAILS_CONTENT_UPDATED)
        {
            fetchPopulatedWorkoutSession();
        }
    }

    void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }

    private void subscribeToPrUpdates()
    {
        Observable.from(workoutSession.getExerciseList())
                .subscribeOn(Schedulers.io())
                .subscribe(exercise -> {
                    DatabasePrCache.getInstance().getPrForExercise(exercise)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(pr -> {
                                fragmentInterface.addPr(pr, exercise);
                            });
                });
    }

    private void fetchPopulatedWorkoutSession() {
        // First fetch todays workout.
        TodaysWorkoutHistoryCache.getInstance()
                .getTodaysWorkoutSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cachedWorkoutSession -> {
                    workoutSession = cachedWorkoutSession;
                    fragmentInterface.updateWorkoutList(cachedWorkoutSession);
                    subscribeToPrUpdates();
                });
    }
}
