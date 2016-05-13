package peoples.materialfitness.Settings.ThemePickerActivity;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;

/**
 * Created by Alex Sullivan on 5/13/2016.
 */
public class ThemePickerPresenter extends BaseActivityPresenter<ThemePickerInterface>
{
    public static class ThemePickerPresenterFactory implements PresenterFactory<ThemePickerPresenter>
    {
        @Override
        public ThemePickerPresenter createPresenter()
        {
            return new ThemePickerPresenter();
        }
    }
}
