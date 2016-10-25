package peoples.materialfitness.Model.Cache;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import rx.Observable;

/**
 * Created by Alex Sullivan on 10/20/2016.
 */

public interface PrCache
{
    Observable<WeightSet> getPrForExercise(Exercise exercise);
    void weightSetModified(WeightSet editedSet, Exercise exercise);
    void weightSetAdded(WeightSet weightSet, Exercise exercise);
}
