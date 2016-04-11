package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Util.AnimationUtils;
import peoples.materialfitness.Util.VersionUtils;
import peoples.materialfitness.WorkoutSession.WorkoutSessionPresenter;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 2/15/16.
 */


public class WorkoutDetailsActivity extends BaseActivity<WorkoutDetailsPresenter>
        implements WorkoutDetailsActivityInterface,
                   MaterialDialog.SingleButtonCallback,
                   AppBarLayout.OnOffsetChangedListener
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    TextView chart;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.middleFab)
    FloatingActionButton middleFab;
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.bottomFab)
    FloatingActionButton bottomFab;

    private boolean bottomFabShown = false;

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
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @OnClick({R.id.middleFab, R.id.bottomFab})
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
    public void showAddSetDialog(int reps, int weight)
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

        repEditText.append(String.valueOf(reps));
        weightEditText.append(String.valueOf(weight));

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
    }

    @Override
    public void addSet(WeightSet set)
    {
        // Add our item.
        int updatedItemPosition = presenter.mExerciseSession.getSets().size() - 1;
        recyclerView.getAdapter().notifyItemInserted(updatedItemPosition);
        // Check to see if we should scroll to our item
        LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        boolean isVisible = updatedItemPosition > layoutManager.findFirstCompletelyVisibleItemPosition()
                && updatedItemPosition < layoutManager.findLastCompletelyVisibleItemPosition();
        if (!isVisible)
        {
            layoutManager.scrollToPosition(updatedItemPosition);
        }
    }

    @Override
    public void contentUpdated(boolean didUpdate)
    {
        setResult(WorkoutSessionPresenter.WORKOUT_DETAILS_CONTENT_UPDATED);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
    {
        presenter.appBarOffsetChanged(appBarLayout.getTotalScrollRange(), verticalOffset);
    }

    @Override
    public void showBottomFab()
    {
        if (bottomFabShown)
        {
            return;
        }
        else
        {
            bottomFabShown = true;
        }
        if (VersionUtils.isLollipopOrGreater())
        {
            AnimationUtils.circularRevealView(bottomFab);
        }
        else
        {
            AnimationUtils.fadeInView(bottomFab);
        }
    }

    @Override
    public void hideBottomFab()
    {
        if (!bottomFabShown)
        {
            return;
        }
        else
        {
            bottomFabShown = false;
        }

        if (VersionUtils.isLollipopOrGreater())
        {
            AnimationUtils.circularHideView(bottomFab);
        }
        else
        {
            AnimationUtils.fadeOutView(bottomFab);
        }
    }
}
