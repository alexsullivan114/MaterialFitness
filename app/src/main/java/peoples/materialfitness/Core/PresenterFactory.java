package peoples.materialfitness.Core;

/**
 * Created by Alex Sullivan on 10/9/2015.
 *
 * Inteface for creating presenters. Each top level view will supply a class that implements this
 * interface. The base class will then use that to create that views presenter and bind everything
 * up.
 */
public interface PresenterFactory<T extends BasePresenter>
{
    T createPresenter();
}
