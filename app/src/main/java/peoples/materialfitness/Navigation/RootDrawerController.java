package peoples.materialfitness.Navigation;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;

import peoples.materialfitness.Model.WeightUnits.WeightUnit;
import peoples.materialfitness.Model.WeightUnits.WeightUnitEvent;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.PreferenceManager;
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
        CompoundButton.OnCheckedChangeListener
{
    private static final String TAG = RootDrawerController.class.getSimpleName();
    private static final int WEIGHT_SWITCH_INDEX = 3;

    private final ActionBarDrawerToggle mDrawerToggle;
    private final BaseActivity mContainingActivity;
    private final DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private NavigationItem navItemToShow = null;
    public NavigationItem currentNavItem;

    public RootDrawerController(BaseActivity drawerActivity,
                                DrawerLayout layout,
                                Toolbar toolbar,
                                Optional<NavigationItem> initialNavItem)
    {
        mDrawerToggle = new ActionBarDrawerToggle(drawerActivity, layout, toolbar, 0, 0);
        mContainingActivity = drawerActivity;
        navigationView = (NavigationView)layout.findViewById(R.id.nav_view);
        drawerLayout = layout;

        layout.setDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        currentNavItem = initialNavItem.isPresent() ? initialNavItem.get() : NavigationItem.NAV_ITEM_LOG_WORKOUT;

        setInitialFragmentSelected();
        setupSettings();
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
        navView.getMenu().getItem(currentNavItem.getPosition()).setChecked(true);
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

        currentNavItem = navItem;

        BaseFragment fragmentToShow = navItem.getFragmentForNavItem();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentToShow.TAG);

        if (currentFragment == null)
        {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.main_fragment, fragmentToShow, fragmentToShow.TAG).commit();

            Log.d(TAG, "Added fragment with tag : " + fragmentToShow.TAG);

            if (fragmentToShow.TAG == null)
            {
                Log.e(TAG, "Null tag.");
            }

            currentNavItem = navItem;
        }
    }

    private void setupSettings()
    {
        SwitchCompat switchCompat = ((SwitchCompat)navigationView.getMenu().getItem(WEIGHT_SWITCH_INDEX).getActionView());
        switchCompat.setOnCheckedChangeListener(this);
        switchCompat.setChecked(PreferenceManager.getInstance().getUnits() == WeightUnit.METRIC);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        WeightUnit newUnit = WeightUnit.METRIC;

        if (!isChecked)
        {
            newUnit = WeightUnit.IMPERIAL;
        }

        PreferenceManager.getInstance().setUnits(newUnit);
        // Post an event to update any interested views!
        EventBus.getDefault().post(new WeightUnitEvent(newUnit));
    }
}
