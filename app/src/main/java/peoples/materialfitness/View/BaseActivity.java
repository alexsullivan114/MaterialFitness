package peoples.materialfitness.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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
    protected String TAG = this.getClass().getSimpleName();
    private String presenterKey = TAG;
    protected Toolbar toolbar;
    protected T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setPresenter(PresenterCache.getInstance().getPresenter(presenterKey, getPresenterFactory()));
        presenter.setActivity(this);
        // TODO: Ok, make this less shitty at some point.
        presenter.setActivityInterface(this);
        Log.i(TAG, "onCreate called");
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
        Log.i(TAG, "onResume called");
    }

    @Override
    public void setContentView(int layoutId)
    {
        super.setContentView(layoutId);
        setToolbar();
        Log.i(TAG, "setContentView called");
        setViewTreeObserver();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        isDestroyedBySystem = true;
        Log.i(TAG, "onSaveInstanceState called");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!isDestroyedBySystem)
        {
            PresenterCache.getInstance().removePresenter(TAG);
        }
        Log.i(TAG, "onDestroy called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void setViewTreeObserver()
    {
        try
        {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            viewGroup.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
            {
                @Override
                public boolean onPreDraw()
                {
                    viewGroup.getViewTreeObserver().removeOnPreDrawListener(this);
                    viewDrawn();
                    return true;
                }
            });
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "Failed to fetch root view of activity. Did you forget to call setContentView?");
        }
    }

    protected void viewDrawn()
    {
    }

    protected boolean showBackInToolbar()
    {
        return true;
    }

    protected abstract PresenterFactory<T> getPresenterFactory();

    private void setPresenter(T presenter)
    {
        this.presenter = presenter;
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackInToolbar());
            getSupportActionBar().setDisplayShowHomeEnabled(showBackInToolbar());
        } else
        {
            Log.w(BASE_TAG, "Could not find toolbar view. Providing class was " +
                    this.getClass().getSimpleName());
        }
    }

}
