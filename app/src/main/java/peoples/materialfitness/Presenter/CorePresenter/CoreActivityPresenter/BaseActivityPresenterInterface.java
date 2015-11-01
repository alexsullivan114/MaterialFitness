package peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter;

import android.app.Activity;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenterInterface;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/24/15.
 */
public interface BaseActivityPresenterInterface<T extends BaseActivityInterface> extends BasePresenterInterface
{
    void setActivity(Activity activity);
    void setActivityInterface(T activityInterface);
}
