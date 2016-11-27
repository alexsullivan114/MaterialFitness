package peoples.materialfitness.Model.Cache;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import rx.Observable;

/**
 * Created by alexscomputerminedonttouch on 11/25/16.
 */

public interface TodaysWorkoutCache
{
    Observable<WorkoutSession> todaysWorkoutObservable();
    void pushWeightSet(final WeightSet weightSet);
    void pushExerciseSession(final ExerciseSession exerciseSession);
    void deleteWeightSet(final WeightSet weightSet);
    void deleteExerciseSession(final ExerciseSession exerciseSession);
    void editWeightSet(final WeightSet weightSet);
}
