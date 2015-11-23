package peoples.materialfitness.Presenter.CorePresenter;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/**
 * Created by Alex Sullivan on 10/10/2015.
 *
 * {@link PresenterCache} is just that - a cache for our Presenters. The idea here is to have some
 * central place to hold all our different presenters. The reason this is necessary is because
 * on orientation change we're really not sure when something is going to be re-created. We go
 * through onSaveInstanceState and then onDestroy if we're fairly confident we're going to stick
 * around. So if we go through onDestroy without going throguh onSaveInstanceState the base view
 * classes will send a message to destroy their copy of the presenter. If we were to just keep
 * the presenter in the activity or fragment then we might get destroyed when we don't want to
 * (i.e. on orientation change).
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
