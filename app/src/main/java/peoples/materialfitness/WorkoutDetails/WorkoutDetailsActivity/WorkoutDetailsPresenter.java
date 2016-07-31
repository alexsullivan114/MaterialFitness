package peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

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

    void deleteSetButtonClicked(int position)
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
            final int oldPrPosition = exerciseSession.getPrPosition();

            WeightSetDatabaseInteractor interactor = new WeightSetDatabaseInteractor();

            interactor.edit(weightSet, exerciseSession.getExercise())
                    .subscribeOn(Schedulers.io())
                    .flatMap(result -> interactor.fetchWithParentId(exerciseSession.getId()))
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(sets -> {
                        exerciseSession.setSets(sets);
                        activityInterface.contentUpdated(true);

                        Optional<WeightSet> maxWeightSet = exerciseSession.getMaxWeightSet();

                        // If we edited our highest weight set for this exercise session we need to
                        // refresh our chart.
                        if (maxWeightSet.isPresent() && maxWeightSet.get().getId().equals(weightSet.getId()))
                        {
                            populateChartData();
                        }

                        if (oldPrPosition != exerciseSession.getPrPosition())
                        {
                            if (oldPrPosition != -1)
                            {
                                activityInterface.refreshSetAtPosition(oldPrPosition);
                            }

                            if (exerciseSession.getPrPosition() != -1)
                            {
                                activityInterface.refreshSetAtPosition(exerciseSession.getPrPosition());
                            }
                        }

                        activityInterface.refreshSetAtPosition(setPosition);
                    });
        }
    }

    void abandonEditing()
    {
        editingSet = Optional.absent();
    }
}
