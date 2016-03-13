package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.parceler.Parcels;

import java.util.Date;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.ExerciseSession;
import peoples.materialfitness.Database.WorkoutSession;
import peoples.materialfitness.Database.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.LogWorkout.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
    implements LogWorkoutDialog.OnExerciseLoggedCallback
{
    public WorkoutSession mWorkoutSession = null;

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
        new WorkoutSessionDatabaseInteractor(MaterialFitnessApplication.getApplication())
                .getTodaysWorkoutSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> {
                    if (mWorkoutSession == null)
                    {
                        mWorkoutSession = new WorkoutSession(DateUtils.getTodaysDate().getTime());
                    }
                    fragmentInterface.updateWorkoutList(mWorkoutSession);
                })
                .doOnError(error -> {
                    Log.d(TAG, error.toString());
                })
                .subscribe(session -> {
                    mWorkoutSession = session;
                });
    }

    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }

    public void onExerciseClicked(ExerciseSession session)
    {
        Intent intent = new Intent(attachedFragment.getActivity(), WorkoutDetailsActivity.class);
        intent.putExtra(WorkoutDetailsPresenter.EXTRA_EXERCISE_SESSION, Parcels.wrap(session));
        attachedFragment.startActivity(intent);
    }

    @Override
    public void onExerciseLogged(Exercise exercise)
    {
        // Check to see if this workout session already contains the exercise...
        if (!mWorkoutSession.containsExercise(exercise))
        {
            // If not add the exercise.
            final ExerciseSession exerciseSession = new ExerciseSession(exercise, new Date().getTime());
            mWorkoutSession.addExerciseSession(exerciseSession);
            // Update our UI
            fragmentInterface.updateExerciseCard(exerciseSession);
            // Fire off a save of the exercise. It won't do anything if we already have it.
            new ExerciseDatabaseInteractor(MaterialFitnessApplication.getApplication()).uniqueSaveExercise(exercise)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(id -> {
                        // Make sure our local exercise copy has the right ID.
                        exercise.setId(id);
                        new WorkoutSessionDatabaseInteractor(MaterialFitnessApplication.getApplication())
                                .cascadeSave(mWorkoutSession)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe();

                    });
        }
    }
}
