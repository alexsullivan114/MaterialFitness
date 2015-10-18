package peoples.materialfitness.View;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.LogWorkoutActivityPresenter;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationUtils;

public class LogWorkoutActivity extends BaseActivity<LogWorkoutActivityPresenter>
{

    @Bind(R.id.fab)
    FloatingActionButton fab;

    MaterialDialog addExerciseDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);
        ButterKnife.bind(this);

    }

    @Override
    public LogWorkoutActivityPresenter.LogWorkoutActivityPresenterFactory getPresenterFactory()
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
        presenter.addWorkout();
    }

    /**
     * Create the muscle group choice alert dialog
     * @param titles titles to display.
     */
    public void createMuscleGroupChoiceDialog(List<String> titles)
    {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this)
                .title(R.string.choose_muscle_group)
                .items(titles.toArray(new String[titles.size()]))
                .autoDismiss(false)
                .itemsCallback((MaterialDialog dialog, View view, int which, CharSequence text) -> {
                    presenter.muscleGroupSelected(String.valueOf(text));
                });

        addExerciseDialog = dialogBuilder.build();
        addExerciseDialog.show();
    }

    public void updateExerciseDialogForMuscleGroup(MuscleGroup muscleGroup)
    {
//        addExerciseDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }
}
