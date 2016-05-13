package peoples.materialfitness.Settings.ThemePickerActivity;

import android.os.Bundle;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 5/13/2016.
 */
public class ThemePickerActivity extends BaseActivity<ThemePickerPresenter> implements ThemePickerInterface
{
    @Override
    protected PresenterFactory<ThemePickerPresenter> getPresenterFactory()
    {
        return new ThemePickerPresenter.ThemePickerPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_picker_layout);
    }
}
