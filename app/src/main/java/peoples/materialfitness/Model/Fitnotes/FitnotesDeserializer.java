package peoples.materialfitness.Model.Fitnotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.MuscleGroup.MuscleGroup;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Alex Sullivan on 4/18/2016.
 * <p>
 * Helper class to deserialize the CSV generated by the Fitnotes app. Currently just for
 * testing purposes.
 */
public class FitnotesDeserializer
{
    private static final int DATE_POSITION = 0;
    private static final int EXERCISE_NAME_POSITION = 1;
    private static final int MUSCLE_GROUP_POSITION = 2;
    private static final int WEIGHT_POSITION = 3;
    private static final int REPS_POSITION = 4;

    public static Observable<WorkoutSession> deserializeFitnotesCsv(final BufferedReader reader)
    {
        return Observable.create(new Observable.OnSubscribe<WorkoutSession>()
        {
            @Override
            public void call(Subscriber<? super WorkoutSession> subscriber)
            {
                if (!subscriber.isUnsubscribed())
                {
                    try
                    {
                        reader.readLine(); //Skip the first line since it's a legend

                        WorkoutSession currentWorkoutSession = new WorkoutSession();
                        ExerciseSession currentExerciseSession = new ExerciseSession();

                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            long date = getDateFromLine(line);
                            if (currentWorkoutSession.getWorkoutSessionDate() != 0)
                            {
                                if (currentWorkoutSession.getWorkoutSessionDate() != date)
                                {
                                    subscriber.onNext(currentWorkoutSession);
                                    currentWorkoutSession = new WorkoutSession();
                                }
                            }
                            else
                            {
                                currentWorkoutSession.setWorkoutSessionDate(date);
                            }

                            Exercise exercise = getExerciseFromString(line);

                            if (currentExerciseSession.getExercise() != null)
                            {
                                if (!currentExerciseSession.getExercise().equals(exercise))
                                {
                                    for (ExerciseSession exerciseSession : currentWorkoutSession.getExerciseSessions())
                                    {
                                        if (exerciseSession.getExercise().equals(exercise))
                                        {
                                            currentWorkoutSession.addExerciseSession(currentExerciseSession);
                                            currentExerciseSession = exerciseSession;
                                            break;
                                        }
                                    }
                                    if (!currentExerciseSession.getExercise().equals(exercise))
                                    {
                                        currentWorkoutSession.addExerciseSession(currentExerciseSession);
                                        currentExerciseSession = new ExerciseSession();
                                        currentExerciseSession.setExercise(exercise);
                                    }
                                }
                            }
                            else
                            {
                                currentExerciseSession.setExercise(exercise);
                            }

                            currentExerciseSession.addSet(getWeightSetFromLine(line));
                        }
                        subscriber.onCompleted();
                    } catch (IOException e)
                    {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    private static WeightSet getWeightSetFromLine(String line)
    {
        String[] tokens = line.split(",");
        String weight = tokens[WEIGHT_POSITION];
        String reps = tokens[REPS_POSITION];

        return new WeightSet(Double.valueOf(weight).intValue(), Double.valueOf(reps).intValue());
    }

    private static long getDateFromLine(String line)
    {
        String[] tokens = line.split(",");
        String dateString = tokens[DATE_POSITION];
        Date date = Date.valueOf(dateString);
        return date.getTime();
    }

    private static Exercise getExerciseFromString(String line)
    {
        String[] tokens = line.split(",");
        String exerciseName = tokens[EXERCISE_NAME_POSITION];
        String muscleGroupName = tokens[MUSCLE_GROUP_POSITION];
        MuscleGroup muscleGroup;
        try
        {
            muscleGroup = MuscleGroup.valueOf(muscleGroupName.toUpperCase());
        } catch (Exception e)
        {
            muscleGroup = MuscleGroup.ARMS;
        }
        return new Exercise(exerciseName, muscleGroup);
    }
}
