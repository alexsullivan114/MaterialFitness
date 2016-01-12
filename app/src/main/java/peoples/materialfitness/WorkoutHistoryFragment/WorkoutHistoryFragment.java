package peoples.materialfitness.WorkoutHistoryFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 12/24/15.
 */
public class WorkoutHistoryFragment extends BaseFragment<WorkoutHistoryFragmentPresenter>
{
    public static WorkoutHistoryFragment newInstance()
    {
        WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected PresenterFactory<WorkoutHistoryFragmentPresenter> getPresenterFactory()
    {
        return new WorkoutHistoryFragmentPresenter.WorkoutHistoryFragmentPresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_workout_history, container, false);
        v.post(new Runnable()
        {
            @Override
            public void run()
            {
                onViewVisible();
            }
        });

        return v;
    }

    private void onViewVisible()
    {
        ((RootFabDisplay)getActivity()).hideFab();
    }
}
