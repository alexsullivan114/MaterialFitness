package peoples.materialfitness.Presenter.RootActivityPresenter;

import peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter.BaseActivityPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Activities.RootActivity.RootActivityInterface;

/**
 * Created by Alex Sullivan on 11/8/2015.
 */
public class RootActivityPresenter extends BaseActivityPresenter<RootActivityInterface>
        implements RootActivityPresenterInterface<RootActivityInterface>
{
    public static class RootActivityPresenterFactory implements PresenterFactory<RootActivityPresenterInterface>
    {
        @Override
        public RootActivityPresenterInterface createPresenter()
        {
            return new RootActivityPresenter();
        }
    }
}
