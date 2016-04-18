package peoples.materialfitness.WorkoutDetails.HistoricalWorkoutDetailsActivity;

import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.WorkoutDetails.WorkoutDetailsPresenter;

/**
 * Created by Alex Sullivan on 4/18/2016.
 */
public class HistoricalWorkoutDetailsPresenter extends WorkoutDetailsPresenter<HistoricalWorkoutDetailsActivityInterface>
{
    public static class HistoricalWorkoutDetailsPresenterFactory implements PresenterFactory<HistoricalWorkoutDetailsPresenter>
    {
        @Override
        public HistoricalWorkoutDetailsPresenter createPresenter()
        {
            return new HistoricalWorkoutDetailsPresenter();
        }
    }
}
