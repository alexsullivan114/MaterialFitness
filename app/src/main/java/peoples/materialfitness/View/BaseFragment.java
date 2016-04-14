package peoples.materialfitness.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

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

    public BaseFragment()
    {
        super();
        setTag();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setPresenter(PresenterCache.getInstance().getPresenter(TAG, getPresenterFactory()));
        presenter.setFragment(this);
        presenter.setFragmentInterface(this);
        Log.d(TAG, "onCreate called");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isDestroyedBySystem = false;
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        isDestroyedBySystem = true;
        Log.d(TAG, "onSaveInstanceState called");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            PresenterCache.getInstance().removePresenter(TAG);
            Log.d(TAG, "Removed presenter for " + TAG);
        }
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        presenter.onContextAvailable(getActivity());
        Log.d(TAG, "onActivityCreated called");
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
