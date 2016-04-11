package peoples.materialfitness.Navigation;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 11/8/2015.
 *
 * {@link RootDrawerController} is a simple controller class for the {@link ActionBarDrawerToggle}.
 * Handles opening and closing the drawer, as well as swapping out the necessary fragments/views
 * when a user clicks into the drawer menu.
 */
public class RootDrawerController implements
        NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener,
        RootFabOnClick
{
    private final ActionBarDrawerToggle mDrawerToggle;
    private final BaseActivity mContainingActivity;
    private final DrawerLayout drawerLayout;

    private NavigationItem navItemToShow = null;
    public NavigationItem currentNavItem = NavigationItem.NAV_ITEM_LOG_WORKOUT;

    public RootDrawerController(BaseActivity drawerActivity,
                                DrawerLayout layout,
                                Toolbar toolbar)
    {
        mDrawerToggle = new ActionBarDrawerToggle(drawerActivity, layout, toolbar, 0, 0);
        mContainingActivity = drawerActivity;
        NavigationView navigationView = (NavigationView)layout.findViewById(R.id.nav_view);
        drawerLayout = layout;

        layout.setDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        setInitialFragmentSelected();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset)
    {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView)
    {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView)
    {
        mDrawerToggle.onDrawerClosed(drawerView);

        if (navItemToShow != null)
        {
            if (navItemToShow != currentNavItem)
            {
                showNavigationItem(navItemToShow);
            }
            navItemToShow = null;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState)
    {
        mDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        navItemToShow = NavigationItem.getNavItemFromMenuItem(item);

        drawerLayout.closeDrawers();
        return true;
    }

    public void setInitialFragmentSelected()
    {
        NavigationView navView = (NavigationView)drawerLayout.findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).setChecked(true);
        showNavigationItem(currentNavItem);
    }

    public void syncState()
    {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfiguration)
    {
        mDrawerToggle.onConfigurationChanged(newConfiguration);
    }

    public void onOptionsItemSelected(MenuItem item)
    {
        mDrawerToggle.onOptionsItemSelected(item);
    }

    public void showNavigationItem(NavigationItem navItem)
    {
        FragmentManager fragmentManager = mContainingActivity.getSupportFragmentManager();

        BaseFragment fragmentToShow = navItem.getFragmentForNavItem();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentToShow.TAG);

        if (currentFragment != null)
        {
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_fragment, fragmentToShow, fragmentToShow.TAG).commit();

        currentNavItem = navItem;
    }

    /**
     * Method to notify whatever fragment is currently being displayed that the fab has been clicked.
     * @param fab Fab that's been clicked
     */
    @Override
    public void onFabClicked(FloatingActionButton fab)
    {
        BaseFragment currentFragment = currentNavItem.getFragmentForNavItem();

        if (currentFragment instanceof RootFabOnClick)
        {
            ((RootFabOnClick)currentFragment).onFabClicked(fab);
        }
        else
        {
            throw new RuntimeException("Fab clicked but currently displayed fragment " +
                    currentFragment.getClass().getSimpleName() + "Doesn't implement RootFabOnClick!");
        }
    }
}
