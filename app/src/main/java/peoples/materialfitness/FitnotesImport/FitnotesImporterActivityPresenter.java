package peoples.materialfitness.FitnotesImport;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import peoples.materialfitness.Core.BaseActivityPresenter;
import peoples.materialfitness.Core.MaterialFitnessApplication;
import peoples.materialfitness.Core.PresenterFactory;
import peoples.materialfitness.Model.Fitnotes.FitnotesDeserializer;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSessionDatabaseInteractor;
import peoples.materialfitness.R;
import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Sullivan on 4/23/16.
 */
public class FitnotesImporterActivityPresenter extends BaseActivityPresenter<FitnotesImporterActivityInterface>
{
    private static final String TAG = FitnotesImporterActivityPresenter.class.getSimpleName();

    public static class FitnotesImporterActivityPresenterFactory implements PresenterFactory<FitnotesImporterActivityPresenter>
    {
        @Override
        public FitnotesImporterActivityPresenter createPresenter()
        {
            return new FitnotesImporterActivityPresenter();
        }
    }

    @Override
    public void setActivityInterface(FitnotesImporterActivityInterface activityInterface)
    {
        super.setActivityInterface(activityInterface);

        deserializeWorkouts();
    }

    private void deserializeWorkouts()
    {
        InputStream inputStream = MaterialFitnessApplication.getApplication().getResources().openRawResource(R.raw.fitnotes_export);
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

        FitnotesDeserializer.deserializeFitnotesCsv(r)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .toList()
                .flatMap(deserializedWorkoutSessions -> {
                    WorkoutSessionDatabaseInteractor interactor = new WorkoutSessionDatabaseInteractor();
                    return interactor.cascadeSaveWorkoutSessions(deserializedWorkoutSessions);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(workoutSession -> {
                    activityInterface.savedWorkoutSession(workoutSession);
                })
                .doOnCompleted(activityInterface::completed)
                .finallyDo(() -> {
                    try
                    {
                        inputStream.close();
                        r.close();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, "Failed to close streams");
                    }
                })
                .subscribe();
    }
}
