package peoples.materialfitness.WorkoutDetails.HistoricalWorkoutDetailsActivity;

import android.os.Bundle;
import android.view.View;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsActivity;

/**
 * Created by Alex Sullivan on 4/18/2016.
 */
public class HistoricalWorkoutDetailsActivity extends WorkoutDetailsActivity<HistoricalWorkoutDetailsPresenter>
{
    @Override
    protected PresenterFactory<HistoricalWorkoutDetailsPresenter> getPresenterFactory()
    {
        return new HistoricalWorkoutDetailsPresenter.HistoricalWorkoutDetailsPresenterFactory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        middleFab.setVisibility(View.GONE);
        bottomFab.setVisibility(View.GONE);
    }
}
