package peoples.materialfitness.Presenter;

import android.app.Activity;
import android.content.Context;

import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 10/9/2015.
 */
public abstract class BaseActivityPresenter<T extends BaseActivityPresenterInterface>
{
    protected T activity;

    public void setActivity(T attachedActivity)
    {
        activity = attachedActivity;
    }

    public Activity getActivityContext()
    {
        try
        {
            return (Activity)activity;
        }
        catch (ClassCastException ex)
        {
            throw new RuntimeException("Base Activity Presenter expects an activity as an attached view");
        }
    }
}
