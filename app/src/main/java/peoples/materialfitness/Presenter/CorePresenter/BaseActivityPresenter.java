package peoples.materialfitness.Presenter.CorePresenter;

import android.app.Activity;
import android.content.Context;

import peoples.materialfitness.View.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 10/9/2015.
 *
 * {@link BaseActivityPresenter}. Ahh Yes. The base activity presenter. Fairly simple (minus my
 * atrocious generic use) base class to handle activity presenters. Mostly exists to avoid the
 * pain of setting presenters in every activity. Let's try to describe the types and the reasons
 * they exist, shall we?
 *
 * <T extends BaseActivityInterface>: This first parameter represents the attached Activity
 * interface. This interface is how the presenter communicates with the activity. Each activity
 * should implement an interface that extends this interface.
 *
 * BaseActivityPresenterInterface<T>: This second parameter is needed because we implement the
 * base activity presenter interface, which is the interface surrounding attaching the actual
 * activity interface to this presenter.
 */
public abstract class BaseActivityPresenter<T extends BaseActivityInterface> extends BasePresenter
{
    protected T activityInterface;
    protected Activity attachedActivity;

    public void setActivity(Activity attachedActivity)
    {
        this.attachedActivity = attachedActivity;
    }

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
