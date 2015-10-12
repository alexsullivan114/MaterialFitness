package peoples.materialfitness.Presenter;

/**
 * Created by alex on 10/9/2015.
 */
public interface PresenterFactory<T extends BaseActivityPresenter>
{
    T createPresenter();
}
