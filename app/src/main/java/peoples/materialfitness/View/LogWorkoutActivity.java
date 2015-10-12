package peoples.materialfitness.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Presenter.LogWorkoutActivityPresenter;
import peoples.materialfitness.R;

public class LogWorkoutActivity extends BaseActivity<LogWorkoutActivityPresenter>
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
    public LogWorkoutActivityPresenter.LogWorkoutActivityPresenterFactory getPresenterFactory()
    {
        return new LogWorkoutActivityPresenter.LogWorkoutActivityPresenterFactory();
    }

    @OnClick(R.id.fab)
    @SuppressWarnings("method unused")
    public void addWorkout(FloatingActionButton fab)
    {
        presenter.addWorkout();
    }

    public void createMuscleGroupChoiceDialog(List<String> titles)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose_muscle_group))
                .setItems(titles.toArray(new String[titles.size()]),
                          (DialogInterface dialog, int which) -> presenter.muscleGroupSelected(titles.get(which)));
        dialogBuilder.show();
    }
}
