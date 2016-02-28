package peoples.materialfitness.Database;

import android.content.ContentValues;

import org.parceler.Parcel;

import java.util.UUID;

/**
 * Created by Alex Sullivan on 10/4/2015.
 *
 * A simple exercise object. This object represents a singular description of an exercise - for
 * example, Squats would be an exercise with obvious accompanying details.
 */
@Parcel(value = Parcel.Serialization.BEAN, analyze = Exercise.class)
public class Exercise
{
    long id;
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

        return exercise;
    }
}
