package peoples.materialfitness.WorkoutDetails.ActiveWorkoutDetailsActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.AnimationHelpers.AnimationUtils;
import peoples.materialfitness.Util.VersionUtils;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

/**
 * Created by Alex Sullivan on 4/18/2016.
 */
public class ActiveWorkoutDetailsActivity extends WorkoutDetailsActivity<ActiveWorkoutDetailsPresenter>
        implements ActiveWorkoutDetailsActivityInterface,
                   MaterialDialog.SingleButtonCallback,
                   AppBarLayout.OnOffsetChangedListener
{

    private boolean bottomFabShown = false;

    @Override
    public PresenterFactory<ActiveWorkoutDetailsPresenter> getPresenterFactory()
    {
        return new ActiveWorkoutDetailsPresenter.ActiveWorkoutDetailsPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(R.string.delete_exercise_session)
                .setIcon(R.drawable.ic_delete_white_24dp)
                .setOnMenuItemClickListener(item -> {
                    presenter.deleteClicked();
                    return true;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

       return true;
    }

    @Override
    public void showDeleteConfirmationView()
    {
        new MaterialDialog.Builder(this)
                .positiveText(R.string.ok)
                .title(R.string.are_you_sure)
                .content(R.string.delete_exercise_session_sure)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    presenter.deleteConfirmClicked();
                }).show();
    }

    @OnClick({R.id.middleFab, R.id.bottomFab})
    public void onFabClicked()
    {
        presenter.fabClicked();
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

        EditText repEditText = (EditText) dialog.findViewById(R.id.reps);
        EditText weightEditText = (EditText) dialog.findViewById(R.id.weight);

        repEditText.append(String.valueOf(reps));
        weightEditText.append(String.valueOf(weight));

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
    }

    @Override
    public void addSet(WeightSet set)
    {
        // Add our item.
        int updatedItemPosition = presenter.exerciseSession.getSets().size() - 1;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction)
    {
        if (dialogAction.equals(DialogAction.POSITIVE))
        {
            View customView = materialDialog.getCustomView();
            EditText reps = (EditText) customView.findViewById(R.id.reps);
            EditText weight = (EditText) customView.findViewById(R.id.weight);
            int repsInt = Integer.parseInt(reps.getText().toString());
            int weightInt = Integer.parseInt(weight.getText().toString());
            presenter.addSet(repsInt, weightInt);
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
            AnimationUtils.circularRevealView(bottomFab, null);
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
            AnimationUtils.circularHideView(bottomFab, null);
        }
        else
        {
            AnimationUtils.fadeOutView(bottomFab);
        }
    }

    @Override
    public void completed()
    {
        finish();
    }

    @Override
    protected boolean allowSetTouchEvents()
    {
        return true;
    }
}
