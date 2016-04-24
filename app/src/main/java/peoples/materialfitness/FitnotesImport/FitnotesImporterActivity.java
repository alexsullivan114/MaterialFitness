package peoples.materialfitness.FitnotesImport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.DateUtils;
import peoples.materialfitness.Util.PreferenceManager;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 4/23/16.
 */
public class FitnotesImporterActivity extends BaseActivity<FitnotesImporterActivityPresenter>
        implements FitnotesImporterActivityInterface
{
    @Bind(R.id.progressTextView)
    TextView progressTextView;

    @Override
    protected PresenterFactory<FitnotesImporterActivityPresenter> getPresenterFactory()
    {
        return new FitnotesImporterActivityPresenter.FitnotesImporterActivityPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitnotes_import_activity);
        ButterKnife.bind(this);
    }

    @Override
    public void deserializingWorkoutSessions()
    {
        progressTextView.setText("Deserializing Fitnotes Workouts");
    }

    @Override
    public void savedWorkoutSession(@NonNull WorkoutSession workoutSession)
    {
        String text = DateUtils.getShortDateDisplayString(workoutSession.getWorkoutSessionDate());
        progressTextView.setText("Saved workout session with date: " + text);
    }

    @Override
    public void completed()
    {
        PreferenceManager.getInstance().setHasBuiltFitnotesDb(true);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        // don't let the user back out of this process.
    }
}
