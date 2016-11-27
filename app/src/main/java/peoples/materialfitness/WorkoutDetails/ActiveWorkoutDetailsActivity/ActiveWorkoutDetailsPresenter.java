package peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Optional;

import java.util.Collections;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightUnits.WeightUnitConverter;
import peoples.materialfitness.Model.Cache.TodaysWorkoutDbCache;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity.WorkoutDetailsPresenter;
import rx.android.schedulers.AndroidSchedulers;
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
        double weight = weightSetOptional.isPresent() ? weightSetOptional.get().getUserUnitsWeight() : 0;

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

    void addSet(int reps, double weight)
    {
        WeightSet set = new WeightSet(WeightUnitConverter.getMetricWeightFromUserInputWeight(weight), reps);
        set.setExerciseSessionId(exerciseSession.getId());
        TodaysWorkoutDbCache.getInstance().pushWeightSet(set);
        exerciseSession.addSet(set);
        activityInterface.addSet(set);
        activityInterface.contentUpdated(true);
        Optional<WeightSet> maxWeightSet = exerciseSession.getMaxWeightSet();
        if (maxWeightSet.isPresent() && maxWeightSet.get().getId().equals(set.getId()))
        {
            populateChartData();
        }
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
                }, (throwable -> {
                    Log.e(TAG, throwable.toString());
                }));
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
        TodaysWorkoutDbCache.getInstance().deleteExerciseSession(exerciseSession);
        activityInterface.contentUpdated(true);
        activityInterface.completed();
    }
}
