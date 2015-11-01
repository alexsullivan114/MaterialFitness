package peoples.materialfitness.Presenter.LogWorkoutActivityPresenter;

import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter.BaseActivityPresenterInterface;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/24/15.
 */
public interface LogWorkoutActivityPresenterInterface<T extends BaseActivityInterface>
        extends BaseActivityPresenterInterface<T>
{
    void addWorkout();
    void muscleGroupSelected(String muscleGroupTitle);
    void setMuscleGroupAdapter(Spinner spinner, MuscleGroup muscleGroup);
    void setExerciseTitleAdapter(AutoCompleteTextView textView, MuscleGroup muscleGroup, MaterialDialog dialog);
    void handleFinalWorkoutCreation(String muscleGroupString, String exerciseNameString);
}
