package peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Model.Cache.DatabasePrCache;
import peoples.materialfitness.Model.Cache.TodaysWorkoutDbCache;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
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
    private Optional<WeightSet> editingSet = Optional.absent();
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
            fetchPrData();
        }
    }

    private void fetchPrData()
    {
        DatabasePrCache.getInstance()
                .getPrForExercise(exerciseSession.getExercise())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weightSet -> {
                    activityInterface.setWeightSetAsPr(weightSet);
                }, (throwable -> {
                    Log.e(TAG, throwable.toString());
                }));
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
                }, (throwable -> {
                    Log.e(TAG, throwable.toString());
                }));
    }

    void deleteSetButtonClicked(int position)
    {
        final WeightSet set = exerciseSession.getSets().get(position);
        TodaysWorkoutDbCache.getInstance().deleteWeightSet(set);
        exerciseSession.getSets().remove(position);
        activityInterface.contentUpdated(true);
        activityInterface.removeSetAtPosition(position);
    }

    void editSetButtonClicked(int position)
    {
        WeightSet weightSet = exerciseSession.getSets().get(position);
        activityInterface.showEditWeightSetDialog(weightSet.getUserUnitsWeight(), weightSet.getNumReps());
        editingSet = Optional.of(weightSet);
    }

    void handleSavedExerciseSession(ExerciseSession savedExerciseSession)
    {
        exerciseSession = savedExerciseSession;
        activityInterface.setTitle(exerciseSession.getExercise().getTitle());
        populateChartData();
    }

    void editSet(final int weight, final int reps)
    {
        if (editingSet.isPresent())
        {
            WeightSet weightSet = editingSet.get();
            weightSet.setUserInputWeight(weight);
            weightSet.setNumReps(reps);

            editingSet = Optional.absent();

            final int setPosition = exerciseSession.getSets().indexOf(weightSet);

            TodaysWorkoutDbCache.getInstance().editWeightSet(weightSet);
            WeightSet oldSet = exerciseSession.getSets().get(setPosition);
            oldSet = weightSet;
            activityInterface.contentUpdated(true);

            Optional<WeightSet> maxWeightSet = exerciseSession.getMaxWeightSet();

            // If we edited our highest weight set for this exercise session we need to
            // refresh our chart.

            // TODO: This doesn't work very well, because there's a race condition between saving off this
            // set and accessing the set in this call. This should be fixed once we redo the workout history
            // cache to work more reactively.
            if (maxWeightSet.isPresent() && maxWeightSet.get().getId().equals(weightSet.getId()))
            {
                populateChartData();
            }

            activityInterface.refreshSetAtPosition(setPosition);
        }
    }

    void abandonEditing()
    {
        editingSet = Optional.absent();
    }
}
