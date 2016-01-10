package peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenter>
    {
        @Override
        public LogWorkoutFragmentPresenter createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }
}
