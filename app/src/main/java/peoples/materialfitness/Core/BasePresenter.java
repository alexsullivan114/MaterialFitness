package peoples.materialfitness.Core;

import android.os.Bundle;

/**
 * Created by Alex Sullivan on 11/1/2015.
 *
 * This is a blank base presenter - basically just something for other presenters to extend.
 *
 * I mostly wanted this hear because I'm sure I'll run into situations where we want some type
 * of functionality that's common to all presenters. Maybe detachedFromView logic, or something
 * along those lines.
 */
public class BasePresenter
{
    protected String TAG;

    public BasePresenter()
    {
        TAG = this.getClass().getSimpleName();
    }

    public void setBundle(Bundle bundle)
    {

    }
}
