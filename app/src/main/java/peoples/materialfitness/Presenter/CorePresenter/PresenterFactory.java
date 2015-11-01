package peoples.materialfitness.Presenter.CorePresenter;

/**
 * Created by Alex Sullivan on 10/9/2015.
 */
public interface PresenterFactory<T extends BasePresenterInterface>
{
    T createPresenter();
}
