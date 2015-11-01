package peoples.materialfitness.Presenter.LogWorkoutActivityPresenter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter.BaseActivityPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.LogWorkoutActivity.LogWorkoutActivityInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public class LogWorkoutActivityPresenter extends BaseActivityPresenter<LogWorkoutActivityInterface>
        implements LogWorkoutActivityPresenterInterface<LogWorkoutActivityInterface>
{
    public static class LogWorkoutActivityPresenterFactory implements PresenterFactory<LogWorkoutActivityPresenterInterface>
    {
        @Override
        public LogWorkoutActivityPresenterInterface createPresenter()
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
        MuscleGroup.getMuscleGroupTitles(getActivityContext()).
                subscribe(activityInterface::createMuscleGroupChoiceDialog);
    }

    /**
     * Initiate the exercise creation process now that we know the muscle group.
     * @param muscleGroupTitle
     */
    public void muscleGroupSelected(String muscleGroupTitle)
    {
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromTitle(muscleGroupTitle, getActivityContext());
        activityInterface.updateExerciseDialogForMuscleGroup(muscleGroup);
    }

    public void createNewExercise(MuscleGroup muscleGroup, CharSequence exerciseName)
    {
        Exercise exercise = new Exercise(String.valueOf(exerciseName), muscleGroup);
        exercise.save();
    }

    public void setMuscleGroupAdapter(Spinner spinner, MuscleGroup muscleGroup)
    {
        ArrayAdapter<String> adapter = generateSimpleArrayAdapter();
        MuscleGroup.getMuscleGroupTitles(getActivityContext()).subscribe(values -> {
            adapter.addAll(values);
            spinner.setAdapter(adapter);
            spinner.setSelection(values.indexOf(muscleGroup.getTitle(getActivityContext())));

        });
    }

    public void setExerciseTitleAdapter(AutoCompleteTextView textView, MuscleGroup muscleGroup, MaterialDialog dialog)
    {
        ArrayAdapter<String> adapter = generateSimpleArrayAdapter();

        String whereClause = Exercise.MUSCLE_GROUP_COLUMN + " = ?";
        String[] arguments = new String[]{String.valueOf(muscleGroup)};

        new ExerciseDatabaseInteractor().fetchWithClause(whereClause, arguments)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .map(Exercise::getTitle)
                .toList()
                .distinct()
                .subscribe(adapter::addAll);

        textView.setAdapter(adapter);
    }

    public void handleFinalWorkoutCreation(String muscleGroupString, String exerciseTitle)
    {
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromTitle(muscleGroupString, getActivityContext());

        Exercise exercise = new Exercise(exerciseTitle, muscleGroup);
        exercise.save();

        activityInterface.exerciseCreated(exercise);
    }

    private ArrayAdapter<String> generateSimpleArrayAdapter()
    {
        return new ArrayAdapter<>(getActivityContext(), android.R.layout.simple_expandable_list_item_1);
    }
}
