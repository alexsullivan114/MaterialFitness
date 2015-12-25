package peoples.materialfitness.View.Activities.RootActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Navigation.RootDrawerController;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.Presenter.RootActivityPresenter.RootActivityPresenter;
import peoples.materialfitness.Presenter.RootActivityPresenter.RootActivityPresenterInterface;
import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreActivity.BaseActivity;

/**
 * Created by Alex Sullivan on 11/8/2015.
 *
 * This class represents the starting activity of the app. It contains the navigation Drawer.
 *
 */
public class RootActivity extends BaseActivity implements RootFabDisplay
{
    @Bind(R.id.drawer)
    DrawerLayout drawer;
    @Bind(R.id.root_fab)
    FloatingActionButton fab;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        mDrawerController.onOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.root_fab)
    public void onFabClicked()
    {
        mDrawerController.onFabClicked(fab);
    }

    @Override
    public void hideFab()
    {
        fab.setVisibility(View.GONE);
    }

    @Override
    public void showFab()
    {
        fab.setVisibility(View.VISIBLE);
    }
}
