package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;

import org.parceler.Parcels;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsPresenter<T extends WorkoutDetailsActivityInterface> extends BaseActivityPresenter<T>
{
    public ExerciseSession mExerciseSession;

    public static final String EXTRA_EXERCISE_SESSION = "extraExercise";

    @Override
    public void setBundle(Bundle bundle)
    {
        super.setBundle(bundle);

        if (bundle != null && bundle.containsKey(EXTRA_EXERCISE_SESSION))
        {
            mExerciseSession = Parcels.unwrap(bundle.getParcelable(EXTRA_EXERCISE_SESSION));
            activityInterface.setTitle(mExerciseSession.getExercise().getTitle());
            populateChartData();
        }
    }

    protected void populateChartData()
    {
        String whereClause = ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID + " = ?";
        String[] args = new String[]{String.valueOf(mExerciseSession.getExercise().getId())};
        new ExerciseSessionDatabaseInteractor().fetchWithClause(whereClause, args)
                .subscribeOn(Schedulers.io())
                .flatMap(exerciseSession -> {
                    String workoutSessionClause = WorkoutSessionContract._ID + " = ?";
                    String[] workoutArgs = new String[]{String.valueOf(exerciseSession.getWorkoutSessionId())};
                    return new WorkoutSessionDatabaseInteractor().fetchWithClause(workoutSessionClause, workoutArgs);
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workoutSessions -> {
                    activityInterface.setChartData(workoutSessions, mExerciseSession.getExercise());
                });
    }
}
