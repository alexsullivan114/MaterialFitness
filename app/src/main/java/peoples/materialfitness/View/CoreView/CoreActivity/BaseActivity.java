package peoples.materialfitness.View.CoreView.CoreActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import peoples.materialfitness.Presenter.CorePresenter.CoreActivityPresenter.BaseActivityPresenterInterface;
import peoples.materialfitness.Presenter.CorePresenter.PresenterCache;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public abstract class BaseActivity<T extends BaseActivityPresenterInterface> extends AppCompatActivity implements BaseActivityInterface
{
    private static final String BASE_TAG = BaseActivity.class.getSimpleName();
    private boolean isDestroyedBySystem;
    protected String TAG;
    protected Toolbar toolbar;
    protected T presenterInterface;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTag();
        setPresenterInterface(PresenterCache.getInstance().getPresenter(TAG, getPresenterFactory()));
        presenterInterface.setActivity(this);
        // TODO: Ok, make this less shitty at some point.
        presenterInterface.setActivityInterface(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isDestroyedBySystem = false;
    }

    @Override
    public void setContentView(int layoutId)
    {
        super.setContentView(layoutId);
        setToolbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        isDestroyedBySystem = true;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            PresenterCache.getInstance().removePresenter(TAG);
        }
    }

    protected abstract PresenterFactory<T> getPresenterFactory();

    private void setPresenterInterface(T presenterInterface)
    {
        this.presenterInterface = presenterInterface;
    }

    private void setTag()
    {
        TAG =  this.getClass().getSimpleName();
    }

    /**
     * Sets the toolbar for the given activity. Assumes a 'toolbar' ID.
     */
    private void setToolbar()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        } else
        {
            Log.w(BASE_TAG, "Could not find toolbar view. Providing class was " +
                    this.getClass().getSimpleName());
        }
    }

}