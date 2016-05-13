package peoples.materialfitness.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 5/12/2016.
 */
public class SettingsActivity extends BaseActivity<SettingsPresenter> implements SettingsViewInterface
{
    @Override
    protected PresenterFactory<SettingsPresenter> getPresenterFactory()
    {
        return new SettingsPresenter.SettingsPresenterFactory();
    }

    public static Intent getStartingIntent(Context startingContext)
    {
        return new Intent(startingContext, SettingsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_down);
    }

    @OnClick(R.id.appearance_setting)
    public void appearanceSettingClicked()
    {
        presenter.appearanceSettingClicked();
    }

    @Override
    public void startCustomColorActivity()
    {

    }
}
