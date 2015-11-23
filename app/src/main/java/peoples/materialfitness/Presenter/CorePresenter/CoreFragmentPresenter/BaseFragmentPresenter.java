package peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter;

import android.app.Fragment;
import android.os.Bundle;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenter;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 *
 * Copy of the base activity presenter but for fragments. The type system follows the same logic.
 */
public abstract class BaseFragmentPresenter<T extends BaseFragmentInterface> extends BasePresenter
    implements BaseFragmentPresenterInterface<T>
{
    protected T fragmentInterface;
    protected Fragment attachedFragment;

    @Override
    public void setFragment(Fragment fragment)
    {
        this.attachedFragment = fragment;
    }

    @Override
    public void setFragmentInterface(T fragmentInterface)
    {
        this.fragmentInterface = fragmentInterface;
    }
}
