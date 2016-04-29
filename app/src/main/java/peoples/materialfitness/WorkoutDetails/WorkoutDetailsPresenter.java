package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsPresenter<T extends WorkoutDetailsActivityInterface> extends BaseActivityPresenter<T>
{
    public ExerciseSession exerciseSession;

    public static final String EXTRA_EXERCISE_SESSION = "extraExercise";

    @Override
    public void setBundle(Bundle bundle)
    {
        super.setBundle(bundle);

        if (bundle != null && bundle.containsKey(EXTRA_EXERCISE_SESSION))
        {
            exerciseSession = Parcels.unwrap(bundle.getParcelable(EXTRA_EXERCISE_SESSION));
            activityInterface.setTitle(exerciseSession.getExercise().getTitle());
            populateChartData();
        }
    }

    protected void populateChartData()
    {
        String whereClause = ExerciseSessionContract.COLUMN_NAME_EXERCISE_ID + " = ?";
        String[] args = new String[]{String.valueOf(exerciseSession.getExercise().getId())};
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
                    activityInterface.setChartData(workoutSessions, exerciseSession.getExercise());
                });
    }

    public void deleteSetButtonClicked(int position)
    {
        final WeightSet set = exerciseSession.getSets().get(position);
        WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();

        interactor.deleteWithPrCheck(set, exerciseSession.getExercise())
                .subscribeOn(Schedulers.io())
                .flatMap(result -> interactor.fetchWithParentId(exerciseSession.getId()))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weightSets -> {
                    Optional<WeightSet> maxWeightSet = exerciseSession.getMaxWeightSet();

                    // If we deleted our highest weight set for this exercise session we need to
                    // refresh our chart.
                    if (maxWeightSet.isPresent() && maxWeightSet.get().getId().equals(set.getId()))
                    {
                        populateChartData();
                    }

                    exerciseSession.setSets(weightSets);
                    // We deleted a PR. Need to check to see if we have a new PR.
                    if (set.getIsPr())
                    {
                        int newPrPosition = exerciseSession.getPrPosition();

                        if (newPrPosition != -1)
                        {
                            activityInterface.refreshSetAtPosition(newPrPosition);
                        }
                    }

                    activityInterface.contentUpdated(true);
                    activityInterface.removeSetAtPosition(position);
                });
    }

    public void editSetButtonClicked(int position)
    {

    }

    public void handleSavedExerciseSession(ExerciseSession savedExerciseSession)
    {
        exerciseSession = savedExerciseSession;
        activityInterface.setTitle(exerciseSession.getExercise().getTitle());
        populateChartData();
    }
}
