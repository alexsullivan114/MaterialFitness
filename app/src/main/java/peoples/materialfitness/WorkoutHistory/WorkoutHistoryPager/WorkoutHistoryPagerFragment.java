package peoples.materialfitness.WorkoutHistory.WorkoutHistoryPager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

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
public class WorkoutHistoryPagerFragment extends BaseFragment<WorkoutHistoryPagerFragmentPresenter> implements WorkoutHistoryPagerFragmentInterface
{
    @Bind(R.id.pager)
    ViewPager pager;

    public static WorkoutHistoryPagerFragment newInstance()
    {
        WorkoutHistoryPagerFragment fragment = new WorkoutHistoryPagerFragment();

        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected PresenterFactory<WorkoutHistoryPagerFragmentPresenter> getPresenterFactory()
    {
        return new WorkoutHistoryPagerFragmentPresenter.WorkoutHistoryFragmentPresenterFactory();
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
