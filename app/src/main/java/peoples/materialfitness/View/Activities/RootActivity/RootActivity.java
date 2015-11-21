package peoples.materialfitness.View.Activities.RootActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Navigation.RootDrawerController;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.RootActivityPresenter.RootActivityPresenter;
import peoples.materialfitness.Presenter.RootActivityPresenter.RootActivityPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivity;

/**
 * Created by Alex Sullivan on 11/8/2015.
 */
public class RootActivity extends BaseActivity
{
    @Bind(R.id.main_fragment)
    FrameLayout mainFragment;
    @Bind(R.id.drawer)
    DrawerLayout drawer;

    private RootDrawerController mDrawerController;

    public PresenterFactory<RootActivityPresenterInterface> getPresenterFactory()
    {
        return new RootActivityPresenter.RootActivityPresenterFactory();
    }

    @Override
    public void onCreate(Bundle instanceState)
    {
        super.onCreate(instanceState);

        setContentView(R.layout.root_activity);
        ButterKnife.bind(this);

        mDrawerController = new RootDrawerController(this, drawer, toolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerController.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerController.onConfigurationChanged(newConfig);
    }
}
