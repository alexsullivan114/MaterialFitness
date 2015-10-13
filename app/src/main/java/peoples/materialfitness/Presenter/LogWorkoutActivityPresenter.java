package peoples.materialfitness.Presenter;
import java.util.List;

import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.View.LogWorkoutActivity;
import rx.Observable;

/**
 * Created by alex on 10/4/2015.
 */
public class LogWorkoutActivityPresenter extends BaseActivityPresenter<LogWorkoutActivity>
{
    public static class LogWorkoutActivityPresenterFactory implements PresenterFactory<LogWorkoutActivityPresenter>
    {
        @Override
        public LogWorkoutActivityPresenter createPresenter()
        {
            return new LogWorkoutActivityPresenter();
        }
    }

    /**
     * Take all muscle groups and build up a list of the titles to then display to the user
     * via an alert dialog.
     */
    public void addWorkout()
    {
        Observable.from(MuscleGroup.class.getEnumConstants())
                .map(muscleGroup -> muscleGroup.getTitle(activity))
                .toList()
                .subscribe(activity::createMuscleGroupChoiceDialog);
    }

    /**
     * Initiate the exercise creation process now that we know the muscle group.
     * @param muscleGroupTitle
     */
    public void muscleGroupSelected(String muscleGroupTitle)
    {
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromTitle(muscleGroupTitle, activity);

    }
}
