package peoples.materialfitness.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 11/8/2015.
 */
public class RootDrawerController
{
    private final ActionBarDrawerToggle mDrawerToggle;
    private final Context mContext;

    public RootDrawerController(Activity drawerActivity, DrawerLayout layout, Toolbar toolbar)
    {
        mDrawerToggle = new ActionBarDrawerToggle(drawerActivity, layout, toolbar, 0, 0);
        mContext = drawerActivity.getApplicationContext();
    }

    public void syncState()
    {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfiguration)
    {
        mDrawerToggle.onConfigurationChanged(newConfiguration);
    }
}
