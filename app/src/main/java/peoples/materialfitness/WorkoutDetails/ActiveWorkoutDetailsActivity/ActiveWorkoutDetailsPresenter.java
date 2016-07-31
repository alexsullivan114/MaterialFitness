package peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Optional;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity.WorkoutDetailsPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 4/18/2016.
 */
class ActiveWorkoutDetailsPresenter extends WorkoutDetailsPresenter<ActiveWorkoutDetailsActivityInterface>
{
    private Optional<WeightSet> lastSessionsFirstWeightSet = Optional.absent();

    static class ActiveWorkoutDetailsPresenterFactory implements PresenterFactory<ActiveWorkoutDetailsPresenter>
    {
        @Override
        public ActiveWorkoutDetailsPresenter createPresenter()
        {
            return new ActiveWorkoutDetailsPresenter();
        }
    }

    @Override
    public void setBundle(Bundle bundle)
    {
        super.setBundle(bundle);
        populateLastSessionFirstWeightSet();
    }

    void fabClicked()
    {
        Optional<WeightSet> weightSetOptional = getDefaultWeightSet();
        int reps = weightSetOptional.isPresent() ? weightSetOptional.get().getNumReps() : 0;
        double weight = weightSetOptional.isPresent() ? weightSetOptional.get().getWeight() : 0;

        activityInterface.hideSetOptions();
        activityInterface.showAddSetDialog(reps, weight);
    }

    private Optional<WeightSet> getDefaultWeightSet()
    {
        if (exerciseSession.getSets().size() > 0)
        {
            return Optional.of(exerciseSession.getSets().get(exerciseSession.getSets().size() - 1));
        }
        else
        {
            return lastSessionsFirstWeightSet;
        }
    }

    void addSet(int reps, int weight)
    {
        WeightSet set = new WeightSet(weight, reps);
        set.setExerciseSessionId(exerciseSession.getId());
        new WeightSetDatabaseInteractor().save(set)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weightSet -> {
                    exerciseSession.addSet(set);
                    activityInterface.addSet(set);
                    activityInterface.contentUpdated(true);
                    // and finally repopulate our chart data if this set is the max weight for
                    // this session.
                    Optional<WeightSet> maxWeightSet = exerciseSession.getMaxWeightSet();
                    if (maxWeightSet.isPresent() && maxWeightSet.get().getId().equals(weightSet.getId()))
                    {
                        populateChartData();
                    }
                });
    }

    /**
     * Look into the database to fetch our last weight set for this exercise type.
     *
     */
    private void populateLastSessionFirstWeightSet()
    {
        new ExerciseSessionDatabaseInteractor()
                .getPreviousExerciseSession(exerciseSession.getExercise())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exerciseSession1 -> {
                    lastSessionsFirstWeightSet = Optional.of(exerciseSession1.getSets().get(0));
                });
    }

    void appBarOffsetChanged(int totalAppBarHeight, int newOffset)
    {
        if (Math.abs(newOffset) >= totalAppBarHeight)
        {
            activityInterface.showBottomFab();
        }
        else
        {
            activityInterface.hideBottomFab();
        }
    }

    void deleteClicked()
    {
        activityInterface.showDeleteConfirmationView();
    }

    void deleteConfirmClicked()
    {
        new ExerciseSessionDatabaseInteractor()
                .cascadeDelete(exerciseSession)
                .subscribeOn(Schedulers.io())
                .toList() //to list so we get something even if there were no weight sets to delete.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    activityInterface.contentUpdated(true);
                    activityInterface.completed();
                });
    }
}
