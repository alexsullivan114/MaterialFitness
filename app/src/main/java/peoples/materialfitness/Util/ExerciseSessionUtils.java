package peoples.materialfitness.Util;

import java.util.List;

import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;

/**
 * Created by alexscomputerminedonttouch on 11/25/16.
 */

public class ExerciseSessionUtils
{
    public static void deleteSetIfPresent(final WeightSet weightSet,
                                          final ExerciseSession exerciseSession)
    {
        if (exerciseSession == null) return;

        for (int i = 0; i < exerciseSession.getSets().size(); i++)
        {
            final WeightSet set = exerciseSession.getSets().get(i);

            if (set.getId().equals(weightSet.getId()))
            {
                exerciseSession.getSets().remove(i);
            }
        }
    }

    public static ExerciseSession getExerciseSessionForSet(final WeightSet weightSet,
                                                           final List<ExerciseSession> exerciseSessionList)
    {
        if (exerciseSessionList == null) return null;

        for (ExerciseSession exerciseSession : exerciseSessionList)
        {
            for (WeightSet set : exerciseSession.getSets())
            {
                if (set.getExerciseSessionId() == exerciseSession.getId())
                {
                    return exerciseSession;
                }
            }
        }

        return null;
    }

    public static WeightSet getWeightSet(final long weightSetId,
                                         final ExerciseSession session)
    {
        if (session == null) return null;

        for (WeightSet set : session.getSets())
        {
            if (set.getId() == weightSetId)
            {
                return set;
            }
        }

        return null;
    }
}
