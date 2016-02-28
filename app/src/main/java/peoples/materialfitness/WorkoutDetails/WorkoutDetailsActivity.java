package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Database.FitnessDatabaseHelper;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 2/15/16.
 */


public class WorkoutDetailsActivity extends BaseActivity<WorkoutDetailsPresenter>
        implements WorkoutDetailsActivityInterface,
        MaterialDialog.SingleButtonCallback
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    FrameLayout chart;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    public PresenterFactory<WorkoutDetailsPresenter> getPresenterFactory()
    {
        return new WorkoutDetailsPresenter.WorkoutDetailsPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_details);
        ButterKnife.bind(this);

        presenter.setBundle(getIntent().getExtras());
    }

    @OnClick(R.id.fab)
    public void onFabClicked()
    {
        presenter.fabClicked();
    }

    @Override
    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void showAddSetDialog()
    {
        new MaterialDialog.Builder(this)
                .title(R.string.add_set)
                .customView(R.layout.add_rep_dialog, false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(this)
                .build()
                .show();
    }

    @Override
    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction)
    {
        if (dialogAction.equals(DialogAction.POSITIVE))
        {
            View customView = materialDialog.getCustomView();
            EditText reps = (EditText)customView.findViewById(R.id.reps);
            EditText weight = (EditText)customView.findViewById(R.id.weight);
            int repsInt = Integer.parseInt(reps.getText().toString());
            int weightInt = Integer.parseInt(weight.getText().toString());
            presenter.addSet(repsInt,weightInt);
        }
    }
}
