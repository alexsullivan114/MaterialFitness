package peoples.materialfitness.LogWorkout.LogWorkoutDialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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
public class LogWorkoutDialog extends MaterialDialog implements
        MaterialDialog.SingleButtonCallback, AdapterView.OnItemClickListener
{
    private TextInputLayout mExerciseTitleLayout;
    private Spinner mMuscleChoiceDialogSpinner;
    private AutoCompleteTextView mExerciseTitleText;
    private Context mContext;
    private OnExerciseLoggedCallback mCallback;

    public LogWorkoutDialog(Context context, OnExerciseLoggedCallback callback)
    {
        super(new MaterialDialog.Builder(context)
                .title(R.string.log_exercise)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .autoDismiss(false)
                .customView(R.layout.create_exercise_dialog, true));

        mBuilder.onAny(this);
        mContext = context;
        mCallback = callback;

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
        mExerciseTitleText.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Exercise exercise = (Exercise)mExerciseTitleText.getAdapter().getItem(position);

        int muscleChoicePosition =
                ((ArrayAdapter<String>)mMuscleChoiceDialogSpinner.getAdapter())
                        .getPosition(exercise.getMuscleGroup().getTitle(mContext));
        mMuscleChoiceDialogSpinner.setSelection(muscleChoicePosition);
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
        new ExerciseDatabaseInteractor(getContext()).fetchAll().toList().subscribe(values -> {
            ExerciseTitleAutoCompleteAdapter adapter =
                    new ExerciseTitleAutoCompleteAdapter(mContext, values);
            mExerciseTitleText.setAdapter(adapter);
        });

        mExerciseTitleText.addTextChangedListener(new TextWatcher()
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
            mCallback.onExerciseLogged(exercise);
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

    public void errorExerciseTitleNull()
    {
        mExerciseTitleLayout.setError(mContext.getResources().getString(R.string.error_empty_exercise));
    }

    public interface OnExerciseLoggedCallback
    {
        void onExerciseLogged(Exercise exercise);
    }
}
