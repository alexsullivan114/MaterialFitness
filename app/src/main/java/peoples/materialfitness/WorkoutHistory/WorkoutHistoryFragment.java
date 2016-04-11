package peoples.materialfitness.WorkoutHistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 12/24/15.
 */
public class WorkoutHistoryFragment extends BaseFragment<WorkoutHistoryFragmentPresenter> implements WorkoutHistoryFragmentInterface
{
    @Bind(R.id.pager)
    ViewPager pager;

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
        ButterKnife.bind(this, v);

        pager.setAdapter(new WorkoutHistoryPagerAdapter(getFragmentManager(), presenter.getWorkoutSessions()));

        v.post(this::onViewVisible);

        return v;
    }

    private void onViewVisible()
    {
        ((RootFabDisplay) getActivity()).hideFab();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setWorkoutSessions(List<WorkoutSession> workoutSessions)
    {
        if (pager != null)
        {
            pager.setAdapter(new WorkoutHistoryPagerAdapter(getFragmentManager(), workoutSessions));
        }
    }
}
