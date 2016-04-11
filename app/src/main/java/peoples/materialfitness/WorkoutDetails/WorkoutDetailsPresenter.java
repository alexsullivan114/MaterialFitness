package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;

import com.google.common.base.Optional;

import org.parceler.Parcels;

import java.util.List;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.Exercise.ExerciseContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionContract;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSessionDatabaseInteractor;
import peoples.materialfitness.Model.ModelDatabaseInteractor;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightSet.WeightSetDatabaseInteractor;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionContract;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsPresenter extends BaseActivityPresenter<WorkoutDetailsActivityInterface>
{
    public ExerciseSession mExerciseSession;
    private Optional<WeightSet> lastSessionsFirstWeightSet = Optional.absent();

    public static final String EXTRA_EXERCISE_SESSION = "extraExercise";

    public static class WorkoutDetailsPresenterFactory implements PresenterFactory<WorkoutDetailsPresenter>
    {
        @Override
        public WorkoutDetailsPresenter createPresenter()
        {
            return new WorkoutDetailsPresenter();
        }
    }

    @Override
    public void setBundle(Bundle bundle)
    {
        super.setBundle(bundle);

        if (bundle != null && bundle.containsKey(EXTRA_EXERCISE_SESSION))
        {
            mExerciseSession = Parcels.unwrap(bundle.getParcelable(EXTRA_EXERCISE_SESSION));
            activityInterface.setTitle(mExerciseSession.getExercise().getTitle());
            populateLastSessionFirstWeightSet();
        }
    }

    public void fabClicked()
    {
        Optional<WeightSet> weightSetOptional = getDefaultWeightSet();
        int reps = weightSetOptional.isPresent() ? weightSetOptional.get().getNumReps() : 0;
        int weight = weightSetOptional.isPresent() ? weightSetOptional.get().getWeight() : 0;

        activityInterface.showAddSetDialog(reps, weight);
    }

    private Optional<WeightSet> getDefaultWeightSet()
    {
        if (mExerciseSession.getSets().size() > 0)
        {
            return Optional.of(mExerciseSession.getSets().get(mExerciseSession.getSets().size() - 1));
        }
        else
        {
            return lastSessionsFirstWeightSet;
        }
    }

    public void addSet(int reps, int weight)
    {
        WeightSet set = new WeightSet(weight, reps);
        set.setExerciseSessionId(mExerciseSession.getId());
        new WeightSetDatabaseInteractor().save(set).subscribe();
        mExerciseSession.addSet(set);
        activityInterface.addSet(set);
        activityInterface.contentUpdated(true);
    }

    /**
     * Look into the database to fetch our last weight set for this exercise type.
     */
    private void populateLastSessionFirstWeightSet()
    {
        // TODO No, we want exercise. Not exercise session.
        String whereClause = ExerciseContract._ID + " = ?";
        String[] args = new String[]{String.valueOf(mExerciseSession.getExercise().getId())};

        new ExerciseSessionDatabaseInteractor()
                .fetchWithClause(whereClause, args)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(session -> {
                    // Fetch the most recent two workout sessions that have this exercise session.
                    // The first should be this exercise session, the second is the one we want.
                    String workoutWhereClause = WorkoutSessionContract._ID + " = ?";
                    String[] workoutArgs = new String[]{String.valueOf(session.getWorkoutSessionId())};
                    String orderingString = WorkoutSessionContract.COLUMN_NAME_DATE + " " + ModelDatabaseInteractor.Ordering.DESC.toString();
                    return new WorkoutSessionDatabaseInteractor().fetchWithArguments(workoutWhereClause,
                            workoutArgs, null, null, null, orderingString, "2");
                })
                .map(WorkoutSession::getExercises)
                .takeLast(1)
                .flatMap(Observable::from)
                .filter(exerciseSession -> exerciseSession.getExercise().equals(mExerciseSession.getExercise()))
                .map(ExerciseSession::getSets)
                .filter(weightSets -> weightSets.size() > 0)
                .subscribe(finalWeightSets -> {
                    lastSessionsFirstWeightSet = Optional.of(finalWeightSets.get(0));
                });
    }

    public void appBarOffsetChanged(int totalAppBarHeight, int newOffset)
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

}
