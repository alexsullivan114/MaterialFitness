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

    public void addWorkout()
    {
        Observable.from(MuscleGroup.class.getEnumConstants())
                .map(muscleGroup -> muscleGroup.getTitle(activity))
                .toList()
                .subscribe(activity::createMuscleGroupChoiceDialog);
    }

    public void muscleGroupSelected(String muscleGroupTitle)
    {
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromTitle(muscleGroupTitle, activity);

    }
}
