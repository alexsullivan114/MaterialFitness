package peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter;

import java.util.List;

import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;
import rx.Observable;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
    implements LogWorkoutFragmentPresenterInterface
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenterInterface>
    {
        @Override
        public LogWorkoutFragmentPresenterInterface createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    @Override
    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }

    @Override
    public Observable<List<String>> getMuscleGroups()
    {
        return MuscleGroup.getMuscleGroupTitles(attachedFragment.getActivity());
    }
}
