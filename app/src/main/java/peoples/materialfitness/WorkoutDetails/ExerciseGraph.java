package peoples.materialfitness.WorkoutDetails;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class ExerciseGraph extends LineChart implements ExerciseGraphInterface
{
    private ExerciseGraphPresenter presenter = new ExerciseGraphPresenter(this);

    public ExerciseGraph(Context context)
    {
        super(context);
        styleChart();
    }

    public ExerciseGraph(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        styleChart();
    }

    public ExerciseGraph(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        styleChart();
    }

    private void styleChart()
    {
        int defaultBackgroundColor = getResources().getColor(R.color.default_background);
        getXAxis().setTextColor(defaultBackgroundColor);
        getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        getXAxis().setAvoidFirstLastClipping(true);
        getAxisLeft().setTextColor(defaultBackgroundColor);
        getAxisRight().setEnabled(false);
        setDescription("");
        setDrawGridBackground(false);
        setDrawBorders(false);
        getLegend().setEnabled(false);
        setNoDataTextDescription("");
        setNoDataText("");
        setTouchEnabled(false);
    }

    public void setWorkoutSessions(List<WorkoutSession> workoutSessions)
    {
        presenter.setWorkoutSessions(workoutSessions);
    }

    public void setExercise(Exercise exercise)
    {
        presenter.setExercise(exercise);
    }

    @Override
    public void setChartData(LineData lineData)
    {
        lineData.setDrawValues(false);
        setData(lineData);
    }
}
