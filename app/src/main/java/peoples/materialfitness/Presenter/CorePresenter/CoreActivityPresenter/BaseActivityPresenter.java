package peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter;

import android.app.Activity;
import android.content.Context;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenter;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/9/2015.
 */
public abstract class BaseActivityPresenter<T extends BaseActivityInterface> extends BasePresenter
        implements BaseActivityPresenterInterface<T>
{
    protected T activityInterface;
    protected Activity attachedActivity;

    @Override
    public void setActivity(Activity attachedActivity)
    {
        this.attachedActivity = attachedActivity;
    }

    @Override
    public void setActivityInterface(T activityInterface)
    {
        this.activityInterface = activityInterface;
    }

    public Activity getActivityContext()
    {
        return attachedActivity;
    }

    public Context getViewContext()
    {
        return attachedActivity;
    }
}
