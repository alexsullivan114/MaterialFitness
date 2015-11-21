package peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter;

import android.app.Fragment;

import peoples.materialfitness.Presenter.CorePresenter.BasePresenterInterface;
import peoples.materialfitness.View.CoreView.BaseViewInterface;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragmentInterface;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public interface BaseFragmentPresenterInterface<T extends BaseFragmentInterface> extends BasePresenterInterface
{
    void setFragment(Fragment fragment);
    void setFragmentInterface(T fragmentInterface);
}
