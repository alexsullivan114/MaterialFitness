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
    public String TAG = this.getClass().getSimpleName();;
    protected T presenter;
    private String presenterKey = TAG;
    private boolean isDestroyedBySystem;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setPresenter(PresenterCache.getInstance().getPresenter(presenterKey, getPresenterFactory()));
        presenter.setFragment(this);
        presenter.setFragmentInterface(this);
        Log.d(TAG, "onCreate called");
    }

    /**
     * This is necessary so that our presenter cache can keep multiple presenters for fragments that are used multiple times
     * in one activity.
     * @param presenterKey The key to use to insert and fetch this views presenter from memory.
     */
    public void setPresenterKey(String presenterKey)
    {
        this.presenterKey = presenterKey;
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
}
