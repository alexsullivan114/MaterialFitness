package peoples.materialfitness.LogWorkout.LogWorkoutDialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.Exercise.ExerciseDatabaseInteractor;
import peoples.materialfitness.Model.MuscleGroup.MuscleGroup;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 1/1/16.
 * <p>
 * This class breaks our MVP pattern. Since we're extending MaterialDialog, I figured rather than
 * hooking everything back up we'd just keep the logic in here for now. If this starts to get
 * unwieldy in the future we can split everything back out.
 */
public class AddExerciseDialog extends MaterialDialog implements
        MaterialDialog.SingleButtonCallback, AdapterView.OnItemClickListener
{
    private TextInputLayout mExerciseTitleLayout;
    private Spinner muscleChoiceDialogSpinner;
    private AutoCompleteTextView exerciseTitleText;
    private Context context;
    private OnExerciseLoggedCallback callback;

    public AddExerciseDialog(Context context, OnExerciseLoggedCallback callback)
    {
        super(new MaterialDialog.Builder(context)
                .title(R.string.log_exercise)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .autoDismiss(false)
                .customView(R.layout.create_exercise_dialog, true));

        mBuilder.onAny(this);
        this.context = context;
        this.callback = callback;

        assignDialogViews();
        setupEditText();
        setDialogAdapters();

    }

    /**
     * The textInputLayout has a somewhat jarring affect the when it receives focus on start.
     * To combat this re remove focus in the XML and re-focus it after a small delay.
     */
    private void setupEditText()
    {
        exerciseTitleText.postDelayed(() -> {
            exerciseTitleText.setFocusableInTouchMode(true);
            exerciseTitleText.requestFocus();
        }, 100);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void assignDialogViews()
    {
        mExerciseTitleLayout = ButterKnife.findById(this, R.id.exercise_layout);
        muscleChoiceDialogSpinner = ButterKnife.findById(this, R.id.muscle_group);
        exerciseTitleText = ButterKnife.findById(this, R.id.exercise_name);
        exerciseTitleText.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Exercise exercise = (Exercise) exerciseTitleText.getAdapter().getItem(position);

        int muscleChoicePosition =
                ((ArrayAdapter<String>) muscleChoiceDialogSpinner.getAdapter())
                        .getPosition(exercise.getMuscleGroup().getTitle(context));
        muscleChoiceDialogSpinner.setSelection(muscleChoicePosition);
    }

    @Override
    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction)
    {
        if (dialogAction == DialogAction.NEGATIVE)
        {
            onNegativeDialogButtonClicked();
        }
        else if (dialogAction == DialogAction.POSITIVE)
        {
            onPositiveDialogButtonClicked(
                    (String) muscleChoiceDialogSpinner.getSelectedItem(),
                    exerciseTitleText.getText().toString());
        }
    }

    /**
     * Sets the relevant adapters on the necessary parts of the dialog
     */
    private void setDialogAdapters()
    {
        setMuscleGroupSpinnerAdapter();
        setExerciseTitleAdapter();
    }

    private void setMuscleGroupSpinnerAdapter()
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        MuscleGroup.getMuscleGroupTitles(context).subscribe(values -> {
            adapter.addAll(values);
            muscleChoiceDialogSpinner.setAdapter(adapter);
        });
    }

    private void setExerciseTitleAdapter()
    {
        // TODO: This should be happening off the main thread...
        new ExerciseDatabaseInteractor().fetchAll().toList().subscribe(values -> {
            ExerciseTitleAutoCompleteAdapter adapter =
                    new ExerciseTitleAutoCompleteAdapter(context, values);
            exerciseTitleText.setAdapter(adapter);
        });

        exerciseTitleText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mExerciseTitleLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void onNegativeDialogButtonClicked()
    {
        dismiss();
    }

    private void onPositiveDialogButtonClicked(final String muscleGroupText, final String exerciseTitle)
    {
        if (!exerciseTitle.isEmpty() && !muscleGroupText.isEmpty())
        {
            Exercise exercise = new Exercise();
            exercise.setTitle(exerciseTitle);
            exercise.setMuscleGroup(MuscleGroup.muscleGroupFromTitle(muscleGroupText, context));
            callback.onExerciseLogged(exercise);
            dismiss();
        }
        else
        {
            if (exerciseTitle.isEmpty())
            {
                errorExerciseTitleNull();
            }
        }
    }

    private void errorExerciseTitleNull()
    {
        mExerciseTitleLayout.setError(context.getResources().getString(R.string.error_empty_exercise));
    }

    public interface OnExerciseLoggedCallback
    {
        void onExerciseLogged(Exercise exercise);
    }
}
