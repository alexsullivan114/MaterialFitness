package peoples.materialfitness.Presenter;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/**
 * Created by alex on 10/10/2015.
 */
public class PresenterCache
{
    private static PresenterCache instance;

    private SimpleArrayMap<String, BaseActivityPresenter> presenterMap = new SimpleArrayMap<>();

    public static PresenterCache getInstance()
    {
        if (instance == null)
        {
            instance = new PresenterCache();
        }

        return instance;
    }

    /**
     * Get the presenter for the given activity. Creates a new presenter if none exists
     * in the cache.
     * @param id unique identifier for this presenter
     * @param presenterFactory Factory to create the presenter if necessary
     * @param <T>
     * @return The presenter
     */
    public final <T extends BaseActivityPresenter> T getActivityPresenter(
            String id, PresenterFactory<T> presenterFactory) {

        T p = null;
        try
        {
            p = (T) presenterMap.get(id);
        } catch (ClassCastException e)
        {
            Log.w("PresenterActivity", "Duplicate Presenter " +
                    "tag identified: " + id + ". This could " +
                    "cause issues with state.");
        }
        if (p == null) {
            p = presenterFactory.createPresenter();
            presenterMap.put(id, p);
        }
        return p;
    }

    /**
     * Removes the presenter with the provided id from the cache
     * @param id id of the presenter to return.
     */
    public final void removePresenter(String id) {
        if (presenterMap != null) {
            presenterMap.remove(id);
        }
    }

}
