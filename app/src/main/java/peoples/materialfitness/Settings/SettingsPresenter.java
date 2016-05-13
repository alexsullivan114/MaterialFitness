package peoples.materialfitness.Settings;

import android.content.Context;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.BaseFragmentPresenter;
import peoples.materialfitness.Core.PresenterFactory;

/**
 * Created by Alex Sullivan on 5/11/2016.
 */
public class SettingsPresenter extends BaseActivityPresenter<SettingsViewInterface>
{
    public static class SettingsPresenterFactory implements PresenterFactory<SettingsPresenter>
    {
        @Override
        public SettingsPresenter createPresenter()
        {
            return new SettingsPresenter();
        }
    }
}
