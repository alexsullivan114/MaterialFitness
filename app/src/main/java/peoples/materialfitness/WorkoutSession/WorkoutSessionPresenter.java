package peoples.materialfitness.WorkoutSession;

import android.content.Intent;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import java.util.Date;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsPresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class WorkoutSessionPresenter<T extends WorkoutSessionFragmentInterface> extends BaseFragmentPresenter<T>
    implements LogWorkoutDialog.OnExerciseLoggedCallback
{
    protected Optional<WorkoutSession> mWorkoutSession = Optional.absent();

    public static final int WORKOUT_DETAILS_REQUEST_CODE = 12312;
    public static final int WORKOUT_DETAILS_CONTENT_UPDATED = 124412;

    protected Subscription todaysWorkoutSubscription;

    public static class WorkoutSessionFragmentPresenterFactory implements PresenterFactory<WorkoutSessionPresenter>
    {
        @Override
        public WorkoutSessionPresenter createPresenter()
        {
            return new WorkoutSessionPresenter();
        }
    }

    public WorkoutSessionPresenter()
    {
        fetchPopulatedWorkoutSession();
    }

    public void onExerciseClicked(ExerciseSession session)
    {
        Intent intent = new Intent(attachedFragment.getActivity(), WorkoutDetailsActivity.class);
        intent.putExtra(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(session));
        fragmentInterface.startWorkoutDetailsActivity(intent, WORKOUT_DETAILS_REQUEST_CODE);
    }

    protected void fetchPopulatedWorkoutSession()
    {
        todaysWorkoutSubscription = new WorkoutSessionDatabaseInteractor()
                .getTodaysWorkoutSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> {
                    if (!mWorkoutSession.isPresent())
                    {
                        mWorkoutSession = Optional.of(new WorkoutSession(DateUtils.getTodaysDate().getTime()));
                    }
                    fragmentInterface.updateWorkoutList(mWorkoutSession.get());
                })
                .subscribe(session -> {
                    mWorkoutSession = Optional.of(session);
                });
    }

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        // Check to see if this workout session already contains the exercise...
        if (!mWorkoutSession.get().containsExercise(exercise))
        {
            // If not add the exercise.
            final ExerciseSession exerciseSession = new ExerciseSession(exercise, new Date().getTime());
            mWorkoutSession.get().addExerciseSession(exerciseSession);
            // Update our UI
            fragmentInterface.updateExerciseCard(exerciseSession);
            // Fire off a save of the exercise. It won't do anything if we already have it.
            new ExerciseDatabaseInteractor().uniqueSaveExercise(exercise)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(savedExercise -> {
                        // Make sure our local exercise copy has the right ID.
                        exercise.setId(savedExercise.getId());
                        new WorkoutSessionDatabaseInteractor()
                                .cascadeSave(mWorkoutSession.get())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe();

                    });
        }
    }

    public Optional<WorkoutSession> getWorkoutSession()
    {
        return mWorkoutSession;
    }
}
