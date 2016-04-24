package peoples.materialfitness.FitnotesImport;

import android.support.annotation.NonNull;

import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseActivityInterface;

/**
 * Created by Alex Sullivan on 4/23/16.
 */
public interface FitnotesImporterActivityInterface extends BaseActivityInterface
{
    void deserializingWorkoutSessions();
    void savedWorkoutSession(@NonNull WorkoutSession workoutSession);
    void completed();
}
