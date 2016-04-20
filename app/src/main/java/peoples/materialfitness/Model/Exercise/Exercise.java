package peoples.materialfitness.Model.Exercise;

import android.content.ContentValues;

import org.parceler.Parcel;

import peoples.materialfitness.Model.MuscleGroup.MuscleGroup;

/**
 * Created by Alex Sullivan on 10/4/2015.
 *
 * A simple exercise object. This object represents a singular description of an exercise - for
 * example, Squats would be an exercise with obvious accompanying details.
 */
@Parcel(value = Parcel.Serialization.BEAN, analyze = Exercise.class)
public class Exercise
{
    long id = -1;
    String title;
    MuscleGroup muscleGroup;

    /**
     * Empty constructor required for sugar record OEM.
     */
    public Exercise()
    {

    }

    @Override
    public String toString()
    {
        return title;
    }

    public Exercise(String title, MuscleGroup muscleGroup)
    {
        this.title = title;
        this.muscleGroup = muscleGroup;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public MuscleGroup getMuscleGroup()
    {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup)
    {
        this.muscleGroup = muscleGroup;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ExerciseContract.COLUMN_NAME_TITLE, title);
        contentValues.put(ExerciseContract.COLUMN_NAME_MUSCLE_GROUP, muscleGroup.getValue());

        return contentValues;
    }

    public static Exercise getExercise(ContentValues contentValues)
    {
        String title = contentValues.getAsString(ExerciseContract.COLUMN_NAME_TITLE);
        MuscleGroup muscleGroup = MuscleGroup.muscleGroupFromValue(
                contentValues.getAsInteger(ExerciseContract.COLUMN_NAME_MUSCLE_GROUP));

        Exercise exercise = new Exercise(title, muscleGroup);
        exercise.setId(contentValues.getAsLong(ExerciseContract._ID));

        return exercise;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise exercise = (Exercise) o;

        if (!title.equals(exercise.title)) return false;
        return muscleGroup.equals(exercise.muscleGroup);

    }

    @Override
    public int hashCode()
    {
        int result = title.hashCode();
        result = 31 * result + muscleGroup.hashCode();
        return result;
    }
}
