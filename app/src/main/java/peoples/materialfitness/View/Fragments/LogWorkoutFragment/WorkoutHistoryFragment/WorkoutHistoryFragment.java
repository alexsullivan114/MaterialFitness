package peoples.materialfitness.View.Fragments.LogWorkoutFragment.WorkoutHistoryFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peoples.materialfitness.Navigation.RootFabOnClick;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.WorkoutHistoryFragmentPresenter.WorkoutHistoryFragmentPresenter;
import peoples.materialfitness.Presenter.WorkoutHistoryFragmentPresenter.WorkoutHistoryFragmentPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragment;

/**
 * Created by Alex Sullivan on 12/24/15.
 */
public class WorkoutHistoryFragment extends BaseFragment<WorkoutHistoryFragmentPresenterInterface>
    implements RootFabOnClick
{
    public static WorkoutHistoryFragment newInstance()
    {
        WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected PresenterFactory<WorkoutHistoryFragmentPresenterInterface> getPresenterFactory()
    {
        return new WorkoutHistoryFragmentPresenter.WorkoutHistoryFragmentPresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_workout_history, container, false);
    }

    @Override
    public void onFabClicked(FloatingActionButton fab)
    {

    }
}
