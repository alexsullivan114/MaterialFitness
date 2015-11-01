package peoples.materialfitness.Presenter.CorePresenter;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/**
 * Created by Alex Sullivan on 10/10/2015.
 */
public class PresenterCache
{
    private static PresenterCache instance;

    private SimpleArrayMap<String, BasePresenterInterface> presenterMap = new SimpleArrayMap<>();

    /**
     * Singleton accessor for the presenterInterface cache.
     * @return Presenter cache.
     */
    public static PresenterCache getInstance()
    {
        if (instance == null)
        {
            instance = new PresenterCache();
        }

        return instance;
    }

    /**
     * Get the presenterInterface for the given activity. Creates a new presenterInterface if none exists
     * in the cache.
     * @param id unique identifier for this presenterInterface
     * @param presenterFactory Factory to create the presenterInterface if necessary
     * @param <T>
     * @return The presenterInterface
     */
    public final <T extends BasePresenterInterface> T getPresenter(
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
     * Removes the presenterInterface with the provided id from the cache
     * @param id id of the presenterInterface to return.
     */
    public final void removePresenter(String id) {
        if (presenterMap != null) {
            presenterMap.remove(id);
        }
    }

}
