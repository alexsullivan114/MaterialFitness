package peoples.materialfitness.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Navigation.RootFabDisplay;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseFragment;

/**
 * Created by Alex Sullivan on 5/14/2016.
 */
public class ScheduleFragment extends BaseFragment<SchedulePresenter>
{
    @Bind(R.id.monday_view)
    TextView mondayView;
    @Bind(R.id.tuesday_view)
    TextView tuesdayView;
    @Bind(R.id.wednesday_view)
    TextView wednesdayView;
    @Bind(R.id.thursday_view)
    TextView thursdayView;
    @Bind(R.id.friday_view)
    TextView fridayView;
    @Bind(R.id.saturday_view)
    TextView saturdayView;
    @Bind(R.id.sunday_view)
    TextView sundayView;

    public static ScheduleFragment newInstance()
    {
        return new ScheduleFragment();
    }

    @Override
    protected PresenterFactory<SchedulePresenter> getPresenterFactory()
    {
        return new SchedulePresenter.SchedulePresenterFactory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.schedule_layout, container, false);

        ButterKnife.bind(this, v);

        ((RootFabDisplay) getActivity()).hideFab();

        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.monday_view, R.id.tuesday_view, R.id.wednesday_view, R.id.thursday_view, R.id.friday_view, R.id.saturday_view, R.id.sunday_view})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.monday_view:
                break;
            case R.id.tuesday_view:
                break;
            case R.id.wednesday_view:
                break;
            case R.id.thursday_view:
                break;
            case R.id.friday_view:
                break;
            case R.id.saturday_view:
                break;
            case R.id.sunday_view:
                break;
        }
    }
}
