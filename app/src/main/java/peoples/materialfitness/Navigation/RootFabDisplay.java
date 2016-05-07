package peoples.materialfitness.Navigation;

import android.support.design.widget.FloatingActionButton;

/**
 * Created by Alex Sullivan on 12/25/15.
 *
 * Interface for hiding, showing, and clicking the root fab.
 */
public interface RootFabDisplay
{
    void hideFab();
    void showFab();
    FloatingActionButton getFab();
}
