package peoples.materialfitness.View.Fragments.LogWorkoutFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.LogWorkoutActivityPresenter.LogWorkoutActivityPresenter;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenter;
import peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter.LogWorkoutFragmentPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragment;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragment extends BaseFragment<LogWorkoutFragmentPresenterInterface>
{
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
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
