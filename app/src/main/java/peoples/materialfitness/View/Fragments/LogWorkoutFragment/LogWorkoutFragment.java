package peoples.materialfitness.View.Fragments.LogWorkoutFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Navigation.RootFabOnClick;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenter;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragment;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragment extends BaseFragment<LogWorkoutFragmentPresenterInterface>
        implements LogWorkoutFragmentInterface, RootFabOnClick, MaterialDialog.SingleButtonCallback
{
    @Bind(R.id.recycler_empty_view)
    TextView recyclerEmptyView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private AutoCompleteTextView muscleChoiceDialogText;
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
        addExerciseDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.log_exercise)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onPositive(this)
                .customView(R.layout.create_exercise_dialog, true).build();

        setDialogAdapters();
        addExerciseDialog.show();


    }

    /**
     * Sets the relevant adapters on the necessary parts of the dialog
     */
    private void setDialogAdapters()
    {
        muscleChoiceDialogText = ButterKnife.findById(addExerciseDialog, R.id.muscle_group);
        exerciseTitleDialogText = ButterKnife.findById(addExerciseDialog, R.id.exercise_name);

        setMuscleGroupSpinnerAdapter();
        setExerciseTitleAdapter();
    }

    private void setMuscleGroupSpinnerAdapter()
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        presenterInterface.getMuscleGroups().subscribe(values -> {
            adapter.addAll(values);
            muscleChoiceDialogText.setAdapter(adapter);
        });
    }

    private void setExerciseTitleAdapter()
    {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        presenterInterface.getExerciseTitles().subscribe(values -> {
            adapter.addAll(values);
            exerciseTitleDialogText.setAdapter(adapter);
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction)
    {
        presenterInterface.onPositiveDialogButtonClicked(muscleChoiceDialogText.getText().toString(),
                exerciseTitleDialogText.getText().toString());
    }
}
