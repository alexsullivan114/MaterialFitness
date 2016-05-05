package peoples.materialfitness.WorkoutDetails.ExerciseGraph;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class ExerciseGraph extends LineChart
        implements ExerciseGraphInterface,
                   OnChartValueSelectedListener
{
    private ExerciseGraphPresenter presenter = new ExerciseGraphPresenter(this);

    private InteractionCallback callback;

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

    public void setCallback(InteractionCallback callback)
    {
        this.callback = callback;
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
        setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
    {
        WorkoutSession associatedSession = (WorkoutSession)e.getData();
        presenter.workoutSessionSelected(associatedSession);
    }

    @Override
    public void onNothingSelected()
    {

    }

    @Override
    public void showHistoricalExerciseSessionDialog(ExerciseSession exerciseSession,
                                                    long exerciseSessionDate)
    {
        callback.showHistoricalExerciseSessionDialog(exerciseSession, exerciseSessionDate);
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
        animateX(2000, Easing.EasingOption.EaseOutQuart);
    }

    public interface InteractionCallback
    {
        void showHistoricalExerciseSessionDialog(ExerciseSession exerciseSession, long exerciseSessionDate);
    }
}
