package peoples.materialfitness.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterCache;
import peoples.materialfitness.Core.PresenterFactory;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public abstract class BaseFragment<T extends BaseFragmentPresenter> extends Fragment
        implements BaseFragmentInterface
{
    private static final String BASE_TAG = BaseFragment.class.getSimpleName();
    public String TAG;
    protected T presenter;
    private boolean isDestroyedBySystem;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTag();
        setPresenter(PresenterCache.getInstance().getPresenter(TAG, getPresenterFactory()));
        presenter.setFragment(this);
        presenter.setFragmentInterface(this);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        presenter.onContextAvailable(getActivity());
    }

    private void setPresenter(T presenter)
    {
        this.presenter = presenter;
    }

    protected abstract PresenterFactory<T> getPresenterFactory();

    private void setTag()
    {
        TAG =  this.getClass().getSimpleName();
    }
}
