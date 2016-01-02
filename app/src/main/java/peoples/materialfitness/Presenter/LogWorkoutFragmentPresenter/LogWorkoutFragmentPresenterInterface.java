package peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter;

import java.util.List;

import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenterInterface;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;
import rx.Observable;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface LogWorkoutFragmentPresenterInterface
        extends BaseFragmentPresenterInterface<LogWorkoutFragmentInterface>
{
    void onFabClicked();
}
