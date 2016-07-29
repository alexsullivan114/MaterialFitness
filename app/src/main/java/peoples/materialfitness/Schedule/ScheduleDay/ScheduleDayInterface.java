package peoples.materialfitness.Schedule.ScheduleDay;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.View.BaseActivityInterface;
import peoples.materialfitness.View.BaseFragmentInterface;

/**
* Created by Alex Sullivan
*/
public interface ScheduleDayInterface extends BaseActivityInterface
{
    void displayWorkoutSession(WorkoutSession workoutSession);
    void showAddExerciseDialog();
    void updateExerciseCard(ExerciseSession exerciseSession);
    void showFab();
    void removeExercise(int position);
}