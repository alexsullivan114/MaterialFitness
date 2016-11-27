package peoples.materialfitness.Util;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by alexscomputerminedonttouch on 11/25/16.
 */

public class WorkoutSessionUtils
{
    public static void removeExerciseSessionIfPresent(final ExerciseSession exerciseSession,
                                                      final WorkoutSession workoutSession)
    {
        for (int i = 0; i < workoutSession.getExerciseSessions().size(); i++)
        {
            final ExerciseSession session = workoutSession.getExerciseSessions().get(i);

            if (session.getId().equals(exerciseSession.getId()))
            {
                workoutSession.getExerciseSessions().remove(i);
            }
        }
    }
}
