package peoples.materialfitness.View.Activities.LogWorkoutActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.LogWorkoutActivityPresenter.LogWorkoutActivityPresenter;
import peoples.materialfitness.Presenter.LogWorkoutActivityPresenter.LogWorkoutActivityPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivity;

public class LogWorkoutActivity extends BaseActivity<LogWorkoutActivityPresenterInterface> implements LogWorkoutActivityInterface
{

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);
        ButterKnife.bind(this);

    }

    @Override
    public PresenterFactory<LogWorkoutActivityPresenterInterface> getPresenterFactory()
    {
        return new LogWorkoutActivityPresenter.LogWorkoutActivityPresenterFactory();
    }

    @OnClick(R.id.fab)
    @SuppressWarnings("method unused")
    /**
     * Initiates the add a workout process.
     */
    public void addWorkout(FloatingActionButton fab)
    {
        presenterInterface.addWorkout();
    }


    /**
     * Create the muscle group choice alert dialog
     * @param titles titles to display.
     */
    public void createMuscleGroupChoiceDialog(List<String> titles)
    {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_muscle_group)
                .items(titles.toArray(new String[titles.size()]))
                .itemsCallback((MaterialDialog dialog, View view, int which, CharSequence text) -> {
                    presenterInterface.muscleGroupSelected(String.valueOf(text));
                })
        .show();
    }

    /**
     * Create a dialog allowing for the creation of an exercise. Uses the provided muscle group
     * as the default option.
     * @param muscleGroup Default muscle group
     */
    public void updateExerciseDialogForMuscleGroup(MuscleGroup muscleGroup)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.create_new_exercise)
                .customView(R.layout.create_exercise_dialog, false)
                .positiveText(R.string.ok)
                .onPositive(this::addExercisePositiveButtonClicked)
                .build();

        setDialogAdapters(dialog, muscleGroup);
        dialog.show();
    }

    /**
     * Sets the relevant adapters on the necessary parts of the dialog
     * @param dialog Dialog to search for relevant views on
     * @param muscleGroup Default muscle group to use for the spinner.
     */
    private void setDialogAdapters(MaterialDialog dialog, MuscleGroup muscleGroup)
    {
        Spinner muscleGroupSpinner = (Spinner)dialog.findViewById(R.id.exercise_muscle_group);
        AutoCompleteTextView exerciseTitle = (AutoCompleteTextView)dialog.findViewById(R.id.exercise_name);

        exerciseTitle.setThreshold(0);

        presenterInterface.setMuscleGroupAdapter(muscleGroupSpinner, muscleGroup);
        presenterInterface.setExerciseTitleAdapter(exerciseTitle, muscleGroup, dialog);
    }

    /**
     * positive button click handler for our exercise creation dialog
     * @param dialog Dialog calling through
     * @param action Action taken
     */
    private void addExercisePositiveButtonClicked(MaterialDialog dialog, DialogAction action)
    {
        Spinner spinner = (Spinner) findViewById(R.id.exercise_muscle_group);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.exercise_name);

        //TODO: Ahhh feel like the presenterInterface shouldn't care about the spinner or the textview.
        // But the view shouldn't care about the adapter or the business logic. Oh well.
        presenterInterface.handleFinalWorkoutCreation((String)spinner.getSelectedItem(), textView.getText().toString());
    }

    public void exerciseCreated(Exercise exercise)
    {

    }
}
