package peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter;

import android.app.Activity;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenterInterface;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/24/15.
 *
 * Simple interface for the Base Activity Presenter. We need this because activities need a reference
 * to something that extends this guy. That's also why it has that type parameter, so that
 * activities that need a reference to their presenter can set the type of the presenter accordingly.
 */
public interface BaseActivityPresenterInterface<T extends BaseActivityInterface> extends BasePresenterInterface
{
    void setActivity(Activity activity);
    void setActivityInterface(T activityInterface);
}
