package peoples.materialfitness.Model.WorkoutSession;

import android.content.ContentValues;
import android.support.v4.util.SimpleArrayMap;


import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.MuscleGroup.MuscleGroup;
import peoples.materialfitness.Util.DateUtils;

/**
 * Created by Alex Sullivan on 10/4/2015.
 *
 * {@link WorkoutSession}'s represent a list of {@link ExerciseSession}'s. This is what really
 * describes a days workout - sets of squats and deadlifts and bench presses and whatnot all come
 * together to make up a {@link WorkoutSession}.
 *
 * Also I think I'm going a little crazy with the links...but I like them!
 *
 */
@Parcel(value = Parcel.Serialization.BEAN)
public class WorkoutSession
{
    private long id = -1;

    private List<ExerciseSession> exerciseSessions = new ArrayList<>();
    // millis since epoch
    private long workoutSessionDate = 0;

    public WorkoutSession()
    {
        // required empty constructor
    }

    public WorkoutSession(long sessionDate)
    {
        this.workoutSessionDate = sessionDate;
    }

    public void addExerciseSession(ExerciseSession session)
    {
        exerciseSessions.add(session);
    }

    public void addAllExerciseSessions(Collection<ExerciseSession> sessions)
    {
        exerciseSessions.addAll(sessions);
    }

    public boolean containsExercise(Exercise exercise)
    {
        for (ExerciseSession session: exerciseSessions)
        {
            if (session.getExercise().getTitle().equalsIgnoreCase(exercise.getTitle()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an exercise session to this workout iff it does not already contain that exercise.
     * @param exercise
     * @return True if the exercise was added, false otherwise.
     */
    public boolean uniqueAddExerciseSession(ExerciseSession exercise)
    {
        if (!containsExercise(exercise.getExercise()))
        {
            addExerciseSession(exercise);
            return true;
        }

        return false;
    }

    /**
     * Adds the exercise session, overwriting the current one if it exists.
     * @param exerciseSession Exercise session to replace the potentially existing one.
     */
    public void setExerciseSession(ExerciseSession exerciseSession)
    {
        for (int i = 0; i < exerciseSessions.size(); i++)
        {
            ExerciseSession existingSession = exerciseSessions.get(i);

            if (existingSession.getExercise().getTitle().equalsIgnoreCase(exerciseSession.getExercise().getTitle()))
            {
                exerciseSessions.set(i, exerciseSession);
                return;
            }
        }

        // If we got this far we didn't replace anything. Add it normally.
        addExerciseSession(exerciseSession);
    }

    public List<ExerciseSession> getExerciseSessions()
    {
        return exerciseSessions;
    }

    public void setExerciseSessions(List<ExerciseSession> exerciseSessions)
    {
        this.exerciseSessions = exerciseSessions;
    }

    public long getWorkoutSessionDate()
    {
        return workoutSessionDate;
    }

    public void setWorkoutSessionDate(long workoutSessionDate)
    {
        this.workoutSessionDate = workoutSessionDate;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    /**
     * Generate a map of muscle group to exercise sessions for this workout session.
     * @return A simple array mapping of muscle groups to exercise sessions.
     */
    public SimpleArrayMap<MuscleGroup, ArrayList<ExerciseSession>> getExerciseMap()
    {
        SimpleArrayMap<MuscleGroup, ArrayList<ExerciseSession>> map = new SimpleArrayMap<>();

        for (ExerciseSession session: exerciseSessions)
        {
            Exercise exercise = session.getExercise();
            ArrayList<ExerciseSession> muscleGroupExercises = map.get(exercise.getMuscleGroup());

            if (muscleGroupExercises == null)
            {
                muscleGroupExercises = new ArrayList<>();
            }

            muscleGroupExercises.add(session);
            map.put(exercise.getMuscleGroup(),muscleGroupExercises);
        }

        return map;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WorkoutSessionContract._ID, id);
        contentValues.put(WorkoutSessionContract.COLUMN_NAME_DATE, workoutSessionDate);

        return contentValues;
    }

    public static WorkoutSession getWorkoutSession(ContentValues contentValues,
                                                   List<ExerciseSession> exercises)
    {
        WorkoutSession workoutSession = new WorkoutSession();

        workoutSession.setId(contentValues.getAsLong(WorkoutSessionContract._ID));
        workoutSession.setWorkoutSessionDate(contentValues.getAsLong(WorkoutSessionContract.COLUMN_NAME_DATE));
        workoutSession.setExerciseSessions(exercises);

        return workoutSession;
    }

    @Override
    public String toString()
    {
        return "Date: " + DateUtils.getShortDateDisplayString(workoutSessionDate);
    }

    public boolean hasSets()
    {
        if (getExerciseSessions() == null || getExerciseSessions().isEmpty())
        {
            return false;
        }

        boolean hasSet = false;

        for (ExerciseSession exerciseSession : getExerciseSessions())
        {
            if (exerciseSession.getSets() != null && !exerciseSession.getSets().isEmpty())
            {
                hasSet = true;
                break;
            }
        }

        return hasSet;
    }

    public List<Exercise> getExerciseList()
    {
        List<Exercise> exercises = new ArrayList<>();

        for (ExerciseSession exerciseSession : exerciseSessions)
        {
            exercises.add(exerciseSession.getExercise());
        }

        return exercises;
    }
}
