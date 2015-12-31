package peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter;

import android.app.Fragment;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenterInterface;
import peoples.materialfitness.View.Fragments.CoreFragment.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 *
 * Once again, basically the same thing as the BaseActivityPresenterInterface.
 */
public interface BaseFragmentPresenterInterface<T extends BaseFragmentInterface>
        extends BasePresenterInterface
{
    void setFragment(Fragment fragment);
    void setFragmentInterface(T fragmentInterface);
}
