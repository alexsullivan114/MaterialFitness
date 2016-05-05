package peoples.materialfitness.WorkoutDetails.ExerciseGraph;

import com.github.mikephil.charting.data.LineData;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.View.BaseViewInterface;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public interface ExerciseGraphInterface extends BaseViewInterface
{
    void setChartData(LineData lineData);
    void showHistoricalExerciseSessionDialog(ExerciseSession exerciseSession, long exerciseSessionDate);
}
