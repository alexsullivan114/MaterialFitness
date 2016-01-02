package peoples.materialfitness.View.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.ButterKnife;
import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 1/1/16.
 * <p>
 * This class breaks our MVP pattern. Since we're extending MaterialDialog, I figured rather than
 * hooking everything back up we'd just keep the logic in here for now. If this starts to get
 * unwieldy in the future we can split everything back out.
 */
public class LogWorkoutDialog extends MaterialDialog implements MaterialDialog.SingleButtonCallback
{
    private TextInputLayout mExerciseTitleLayout;
    private Spinner mMuscleChoiceDialogSpinner;
    private AutoCompleteTextView mExerciseTitleText;
    private Context mContext;

    public LogWorkoutDialog(Context context)
    {
        super(new MaterialDialog.Builder(context)
                .title(R.string.log_exercise)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .autoDismiss(false)
                .customView(R.layout.create_exercise_dialog, true));

        mBuilder.onAny(this);
        mContext = context;

        assignDialogViews();
        setupEditText();
        setDialogAdapters();

    }

    private void setupEditText()
    {
        mExerciseTitleText.postDelayed(() -> {
            mExerciseTitleText.setFocusableInTouchMode(true);
            mExerciseTitleText.requestFocus();
        }, 100);
    }

    private void assignDialogViews()
    {
        mExerciseTitleLayout = ButterKnife.findById(this, R.id.exercise_layout);
        mMuscleChoiceDialogSpinner = ButterKnife.findById(this, R.id.muscle_group);
        mExerciseTitleText = ButterKnife.findById(this, R.id.exercise_name);
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
                    (String) mMuscleChoiceDialogSpinner.getSelectedItem(),
                    mExerciseTitleText.getText().toString());
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
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item);
        MuscleGroup.getMuscleGroupTitles(mContext).subscribe(values -> {
            adapter.addAll(values);
            mMuscleChoiceDialogSpinner.setAdapter(adapter);
        });
    }

    private void setExerciseTitleAdapter()
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1);
        new ExerciseDatabaseInteractor().getExerciseTitles().subscribe(values -> {
            adapter.addAll(values);
            mExerciseTitleText.setAdapter(adapter);
        });
    }

    public void onNegativeDialogButtonClicked()
    {
        dismiss();
    }

    public void onPositiveDialogButtonClicked(final String muscleGroupText, final String exerciseTitle)
    {
        if (!exerciseTitle.isEmpty() && !muscleGroupText.isEmpty())
        {
            Exercise exercise = new Exercise();
            exercise.setTitle(exerciseTitle);
            exercise.setMuscleGroup(MuscleGroup.muscleGroupFromTitle(muscleGroupText, mContext));
            new ExerciseDatabaseInteractor().uniqueSaveExercise(exercise);
            dismiss();
        }
        else
        {
            if (exerciseTitle.isEmpty())
            {
                errorExerciseTitleNull();
            }

            if (muscleGroupText.isEmpty())
            {
                errorMuscleGroupTextNull();
            }
        }
    }

    public void errorExerciseTitleNull()
    {
        mExerciseTitleLayout.setError(mContext.getResources().getString(R.string.error_empty_exercise));
    }

    // TODO: Handle this situation.
    public void errorMuscleGroupTextNull()
    {
//            TextInputLayout inputLayout = ButterKnife.findById(addExerciseDialog, R.id.muscle_group_layout);
//            inputLayout.setError(mContext.getResources().getString(R.string.error_empty_muscle_group));
    }
}
