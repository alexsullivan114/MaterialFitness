package peoples.materialfitness.WorkoutDetails.ExerciseGraph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import peoples.materialfitness.Core.BasePresenter;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Util.DateUtils;

/**
 * Created by Alex Sullivan on 4/11/2016.
 */
public class ExerciseGraphPresenter extends BasePresenter
{
    private List<WorkoutSession> workoutSessions;
    private Exercise exercise;
    private ExerciseGraphInterface viewInterface;

    ExerciseGraphPresenter(ExerciseGraphInterface viewInterface)
    {
        this.viewInterface = viewInterface;
    }

    public void setWorkoutSessions(List<WorkoutSession> workoutSessions)
    {
        this.workoutSessions = workoutSessions;
        calculateChartData();
    }

    void workoutSessionSelected(WorkoutSession workoutSession)
    {
        for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
        {
            if (exerciseSession.getExercise().equals(exercise))
            {
                viewInterface.showHistoricalExerciseSessionDialog(exerciseSession, workoutSession.getWorkoutSessionDate());
                break;
            }
        }
    }

    public void setExercise(Exercise exercise)
    {
        this.exercise = exercise;
    }

    private void calculateChartData()
    {
        sortWorkoutSessions();
        List<Entry> entries = calculateChartEntries();
        List<String> xValues = getXLabelValuesFromEntries(entries);
        LineDataSet lineDataSet = new LineDataSet(entries, "Weight, yo");
        lineDataSet.setDrawHighlightIndicators(false);
        LineData lineData = new LineData(xValues, Collections.singletonList(lineDataSet));
        viewInterface.setChartData(lineData);
    }

    private List<String> getXLabelValuesFromEntries(List<Entry> entries)
    {
        ArrayList<String> returnList = new ArrayList<>();

        for (Entry entry : entries)
        {
            String formattedDate = DateUtils.getShortDateDisplayString(((WorkoutSession)entry.getData()).getWorkoutSessionDate());
            returnList.add(formattedDate);
        }

        return returnList;
    }

    private void sortWorkoutSessions()
    {
        Collections.sort(workoutSessions, (lhs, rhs) -> {
            if (rhs.getWorkoutSessionDate() < lhs.getWorkoutSessionDate())
            {
                return 1;
            }
            else if (rhs.getWorkoutSessionDate() > lhs.getWorkoutSessionDate())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        });
    }

    private List<Entry> calculateChartEntries()
    {
        List<Entry> returnList = new ArrayList<>();
        // Loop through all of our workout sessions to find the relevant exercise sessions
        for (int i = 0; i < workoutSessions.size(); i++)
        {
            WorkoutSession workoutSession = workoutSessions.get(i);
            // Loop through all of our exercise sessions associated with this workout session
            // to find the exercise
            for (ExerciseSession exerciseSession : workoutSession.getExerciseSessions())
            {
                if (exerciseSession.getExercise().equals(exercise))
                {
                    // Now find the max weight from this exercise session.
                    double maxWeight = 0;

                    for (WeightSet weightSet : exerciseSession.getSets())
                    {
                        if (weightSet.getUserUnitsWeight() > maxWeight)
                        {
                            maxWeight = weightSet.getUserUnitsWeight();
                        }
                    }
                    // and finally construct our entry.
                    returnList.add(new Entry((float)maxWeight, i, workoutSession));
                }
            }
        }

        return returnList;
    }
}
