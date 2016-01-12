package peoples.materialfitness.Presenter.CorePresenter;

import android.app.Fragment;

import peoples.materialfitness.View.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 *
 * Copy of the base activity presenter but for fragments. The type system follows the same logic.
 */
public abstract class BaseFragmentPresenter<T extends BaseFragmentInterface> extends BasePresenter
{
    protected T fragmentInterface;
    protected Fragment attachedFragment;

    public void setFragment(Fragment fragment)
    {
        this.attachedFragment = fragment;
    }

    public void setFragmentInterface(T fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }
}
