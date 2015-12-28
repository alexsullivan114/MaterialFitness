package peoples.materialfitness.Presenter.LogWorkoutFragmentPresenter;

import java.util.List;

import peoples.materialfitness.Database.Exercise;
import peoples.materialfitness.Database.ExerciseDatabaseInteractor;
import peoples.materialfitness.Database.MuscleGroup;
import peoples.materialfitness.Presenter.CorePresenter.CoreFragmentPresenter.BaseFragmentPresenter;
import peoples.materialfitness.Presenter.CorePresenter.PresenterFactory;
import peoples.materialfitness.View.Fragments.LogWorkoutFragment.LogWorkoutFragmentInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 11/21/15.
 */
public class LogWorkoutFragmentPresenter extends BaseFragmentPresenter<LogWorkoutFragmentInterface>
    implements LogWorkoutFragmentPresenterInterface
{
    public static class LogWorkoutFragmentPresenterFactory implements PresenterFactory<LogWorkoutFragmentPresenterInterface>
    {
        @Override
        public LogWorkoutFragmentPresenterInterface createPresenter()
        {
            return new LogWorkoutFragmentPresenter();
        }
    }

    @Override
    public void onFabClicked()
    {
        fragmentInterface.showAddWorkoutDialog();
    }

    @Override
    public Observable<List<String>> getExerciseTitles()
    {
        return new ExerciseDatabaseInteractor().fetchAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .map(Exercise::getTitle)
                .toList()
                .distinct();
    }

    @Override
    public void onPositiveDialogButtonClicked(final String muscleGroupText, final String exerciseTitle)
    {
        saveExerciseIfNecessary(muscleGroupText, exerciseTitle);
    }

    private void saveExerciseIfNecessary(final String muscleGroupText, final String exerciseTitle)
    {

        String whereClause = Exercise.TITLE_COLUMN + " = ?";
        String[] arguments = new String[]{String.valueOf(exerciseTitle)};

        new ExerciseDatabaseInteractor().fetchWithClause(whereClause, arguments)
                .subscribeOn(Schedulers.newThread())
                .map(Exercise::getTitle)
                .toList()
                .distinct()
                .subscribe(values -> {
                    if (!values.contains(exerciseTitle))
                    {
                        if (attachedFragment.getActivity() != null)
                        {
                            createAndSaveExercise(exerciseTitle, muscleGroupText);
                        }
                    }
                });
    }

    private void createAndSaveExercise(String exerciseTitle, String muscleGroupTitle)
    {
        Exercise newExercise = new Exercise(exerciseTitle,
                MuscleGroup.muscleGroupFromTitle(muscleGroupTitle, attachedFragment.getActivity()));
        newExercise.save();
    }

    @Override
    public Observable<List<String>> getMuscleGroups()
    {
        return MuscleGroup.getMuscleGroupTitles(attachedFragment.getActivity());
    }
}
