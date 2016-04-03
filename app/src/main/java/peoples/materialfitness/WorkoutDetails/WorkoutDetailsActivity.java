package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.LogWorkout.LogWorkoutFragment.LogWorkoutFragmentPresenter;
import peoples.materialfitness.Model.WeightSet.WeightSet;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WorkoutDetailsRecyclerAdapter(presenter.mExerciseSession));
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
    public void showAddSetDialog(String repsText, String weightText)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.add_set)
                .customView(R.layout.add_rep_dialog, false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(this)
                .build();

        EditText repEditText = (EditText)dialog.findViewById(R.id.reps);
        EditText weightEditText = (EditText)dialog.findViewById(R.id.weight);

        repEditText.append(repsText);
        weightEditText.append(weightText);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        dialog.show();
    }

    @Override
    public void addSet(WeightSet set)
    {
        int updatedItemPosition = presenter.mExerciseSession.getSets().size() - 1;
        recyclerView.getAdapter().notifyItemInserted(updatedItemPosition);
        recyclerView.getLayoutManager().scrollToPosition(updatedItemPosition);
    }

    @Override
    public void contentUpdated(boolean didUpdate)
    {
        setResult(LogWorkoutFragmentPresenter.WORKOUT_DETAILS_CONTENT_UPDATED);
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
