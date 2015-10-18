package peoples.materialfitness.Presenter;

import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 10/9/2015.
 */
public class BaseActivityPresenter<T extends BaseActivity>
{
    protected T activity;

    public void setActivity(T attachedActivity)
    {
        activity = attachedActivity;
    }
}
