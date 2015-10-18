package peoples.materialfitness.Presenter;

/**
 * Created by Alex Sullivan on 10/9/2015.
 */
public interface PresenterFactory<T extends BaseActivityPresenter>
{
    T createPresenter();
}
