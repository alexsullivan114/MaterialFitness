package peoples.materialfitness.Presenter.RootActivityPresenter;

import peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter.BaseActivityPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Activities.RootActivity.RootActivityInterface;

/**
 * Created by Alex Sullivan on 11/8/2015.
 *
 * The root activity presenter. This presenter will mostly handle the logic for deeplinking into
 * the app as well as handling navigation changes when a user selects something from the nav drawer.
 */
public class RootActivityPresenter extends BaseActivityPresenter<RootActivityInterface>
{
    public static class RootActivityPresenterFactory implements PresenterFactory<RootActivityPresenter>
    {
        @Override
        public RootActivityPresenter createPresenter()
        {
            return new RootActivityPresenter();
        }
    }
}
