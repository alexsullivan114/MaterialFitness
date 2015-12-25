package peoples.materialfitness.Navigation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import peoples.materialfitness.R;
import peoples.materialfitness.View.CoreView.CoreFragment.BaseFragment;

/**
 * Created by Alex Sullivan on 11/8/2015.
 *
 * {@link RootDrawerController} is a simple controller class for the {@link ActionBarDrawerToggle}.
 * Handles opening and closing the drawer, as well as swapping out the necessary fragments/views
 * when a user clicks into the drawer menu.
 */
public class RootDrawerController implements
        NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener
{
    private final ActionBarDrawerToggle mDrawerToggle;
    private final Activity mContainingActivity;
    private final DrawerLayout drawerLayout;

    private NavigationItem navItemToShow;

    public RootDrawerController(Activity drawerActivity,
                                DrawerLayout layout,
                                Toolbar toolbar)
    {
        mDrawerToggle = new ActionBarDrawerToggle(drawerActivity, layout, toolbar, 0, 0);
        mContainingActivity = drawerActivity;
        NavigationView navigationView = (NavigationView)layout.findViewById(R.id.nav_view);
        drawerLayout = layout;

        layout.setDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);
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
            showNavigationItem(navItemToShow);
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

    private void showNavigationItem(NavigationItem navItem)
    {
        FragmentManager fragmentManager = mContainingActivity.getFragmentManager();

        BaseFragment fragmentToShow = navItem.getFragmentForNavItem();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentToShow.TAG);

        if (currentFragment != null)
        {
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        }

        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.main_fragment, fragmentToShow, fragmentToShow.TAG).commit();

    }
}
