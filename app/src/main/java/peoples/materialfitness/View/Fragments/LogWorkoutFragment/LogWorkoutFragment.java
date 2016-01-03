package peoples.materialfitness.View.Fragments.LogWorkoutFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Navigation.RootFabOnClick;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenter;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.Fragments.LogWorkoutDialog.LogWorkoutDialog;
import peoples.materialfitness.View.Fragments.CoreFragment.BaseFragment;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragment extends BaseFragment<LogWorkoutFragmentPresenterInterface>
        implements LogWorkoutFragmentInterface, RootFabOnClick
{
    @Bind(R.id.recycler_empty_view)
    TextView recyclerEmptyView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private TextInputLayout muscleChoiceLayout;
    private TextInputLayout exerciseTitleLayout;
    private Spinner muscleChoiceDialogText;
    private AutoCompleteTextView exerciseTitleDialogText;

    private MaterialDialog addExerciseDialog;


    @Override
    public PresenterFactory<LogWorkoutFragmentPresenterInterface> getPresenterFactory()
    {
        return new LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterFactory();
    }

    public static LogWorkoutFragment newInstance()
    {
        LogWorkoutFragment fragment = new LogWorkoutFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_log_workout, container, false);
        ButterKnife.bind(this, v);

        v.post(this::onViewVisible);
        return v;
    }

    private void onViewVisible()
    {
        ((RootFabDisplay) getActivity()).showFab();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onFabClicked(FloatingActionButton fab)
    {
        presenterInterface.onFabClicked();
    }

    @Override
    public void showAddWorkoutDialog()
    {
        LogWorkoutDialog dialog = new LogWorkoutDialog(getActivity());
        dialog.show();

    }
}
