package peoples.materialfitness.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterCache;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 10/4/2015.
 */
public abstract class BaseActivity<T extends BaseActivityPresenter> extends AppCompatActivity
        implements BaseActivityInterface
{
    private static final String BASE_TAG = BaseActivity.class.getSimpleName();
    private boolean isDestroyedBySystem;
    protected String TAG;
    protected Toolbar toolbar;
    protected T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTag();
        setPresenter(PresenterCache.getInstance().getPresenter(TAG, getPresenterFactory()));
        presenter.setActivity(this);
        // TODO: Ok, make this less shitty at some point.
        presenter.setActivityInterface(this);
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

    private void setPresenter(T presenter)
    {
        this.presenter = presenter;
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
