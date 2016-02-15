package peoples.materialfitness.WorkoutDetails;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.R;
import peoples.materialfitness.View.BaseActivity;

/**
 * Created by Alex Sullivan on 2/15/16.
 */


public class WorkoutDetailsActivity extends BaseActivity<WorkoutDetailsPresenter>
        implements WorkoutDetailsActivityInterface
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    FrameLayout chart;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    public PresenterFactory<WorkoutDetailsPresenter> getPresenterFactory()
    {
        return new WorkoutDetailsPresenter.WorkoutDetailsPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_details);
        ButterKnife.bind(this);
        presenter.setBundle(getIntent().getExtras());
    }

    @Override
    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
}
