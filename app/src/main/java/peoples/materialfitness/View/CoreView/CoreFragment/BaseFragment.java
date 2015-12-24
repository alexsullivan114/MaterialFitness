package peoples.materialfitness.View.CoreView.CoreFragment;

import android.app.Fragment;
import android.os.Bundle;

import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenterInterface;
import peoples.materialfitness.Presenter.CorePresenter.PresenterCache;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public abstract class BaseFragment<T extends BaseFragmentPresenterInterface> extends Fragment implements BaseFragmentInterface
{
    private static final String BASE_TAG = BaseFragment.class.getSimpleName();
    public String TAG;
    protected T presenterInterface;
    private boolean isDestroyedBySystem;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTag();
        setPresenterInterface(PresenterCache.getInstance().getPresenter(TAG, getPresenterFactory()));
        presenterInterface.setFragment(this);
        // TODO: Ok, make this less shitty at some point.
        presenterInterface.setFragmentInterface(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isDestroyedBySystem = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        isDestroyedBySystem = true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            PresenterCache.getInstance().removePresenter(TAG);
        }
    }

    private void setPresenterInterface(T presenterInterface)
    {
        this.presenterInterface = presenterInterface;
    }

    protected abstract PresenterFactory<T> getPresenterFactory();

    private void setTag()
    {
        TAG =  this.getClass().getSimpleName();
    }
}
