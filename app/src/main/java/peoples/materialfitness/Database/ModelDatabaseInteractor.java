package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;

/**
 * Created by Alex Sullivan on 10/18/2015.
 */
public interface ModelDatabaseInteractor<T>
{
    Observable<T> fetchAll();
    Observable<T> fetchWithClause(String whereClause, String[] arguments);
    void save(T entity);
    void delete(T entity);
}
