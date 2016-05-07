package peoples.materialfitness.RootScreen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.google.common.base.Optional;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.BuildConfig;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.FitnotesImport.FitnotesImporterActivity;
import peoples.materialfitness.Navigation.NavigationItem;
import peoples.materialfitness.Navigation.RootDrawerController;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.Constants;
import peoples.materialfitness.Util.PreferenceManager;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 11/8/2015.
 *
 * This class represents the starting activity of the app. It contains the navigation Drawer.
 *
 */
public class RootActivity extends BaseActivity implements RootFabDisplay
{
    private static final String NAV_ITEM_KEY = "currentNavItemKey";

    @Bind(R.id.drawer)
    DrawerLayout drawer;
    @Bind(R.id.root_fab)
    FloatingActionButton fab;

    private RootDrawerController mDrawerController;

    public PresenterFactory<RootActivityPresenter> getPresenterFactory()
    {
        return new RootActivityPresenter.RootActivityPresenterFactory();
    }

    @Override
    public void onCreate(Bundle instanceState)
    {
        super.onCreate(instanceState);

        setContentView(R.layout.root_activity);
        ButterKnife.bind(this);

        Optional<NavigationItem> savedNavItem = Optional.absent();
        if (instanceState != null && instanceState.containsKey(NAV_ITEM_KEY))
        {
            savedNavItem = Optional.of((NavigationItem)instanceState.getSerializable(NAV_ITEM_KEY));
        }

        mDrawerController = new RootDrawerController(this, drawer, toolbar, savedNavItem);

        if (BuildConfig.BUILD_TYPE.equals(Constants.IMPORT_FITNOTES_BUILD_TYPE)
                && !PreferenceManager.getInstance().getHasBuiltFitnotesDb())
        {
            Intent intent = new Intent(this, FitnotesImporterActivity.class);
            startActivity(intent);
        }
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
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable(NAV_ITEM_KEY, mDrawerController.currentNavItem);
        super.onSaveInstanceState(outState);
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
        if (fab.isShown())
        {
            fab.post(() -> fab.hide());
        }
    }

    @Override
    protected boolean showBackInToolbar()
    {
        return false;
    }

    @Override
    public void showFab()
    {
        if (!fab.isShown())
        {
            fab.post(() -> fab.show());
        }
    }

    @Override
    public FloatingActionButton getFab()
    {
        return fab;
    }
}
