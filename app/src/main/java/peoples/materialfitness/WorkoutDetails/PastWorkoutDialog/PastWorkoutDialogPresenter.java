package peoples.materialfitness.WorkoutDetails.PastWorkoutDialog;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.DateUtils;

/**
 * Created by Alex Sullivan on 5/4/2016.
 */
public class PastWorkoutDialogPresenter extends BaseActivityPresenter<PastWorkoutDialogInterface>
{
    private long millisSinceEpoch;
    private ExerciseSession exerciseSession;

    public static class PastWorkoutDialogPresenterFactory implements PresenterFactory<PastWorkoutDialogPresenter>
    {
        @Override
        public PastWorkoutDialogPresenter createPresenter()
        {
            return new PastWorkoutDialogPresenter();
        }
    }

    public String getTitle()
    {
        return getActivityContext().getString(R.string.date_exercise_title,
                                              DateUtils.getShortDateDisplayString(millisSinceEpoch),
                                              exerciseSession.getExercise().getTitle());
    }

    public void setWorkoutSessionDate(long millisSinceEpoch)
    {
        this.millisSinceEpoch = millisSinceEpoch;
    }

    public void setExerciseSession(ExerciseSession exerciseSession)
    {
        this.exerciseSession = exerciseSession;
        activityInterface.setupRecyclerView(this.exerciseSession);
    }
}
