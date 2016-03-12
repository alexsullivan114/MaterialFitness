package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;

/**
 * Created by Alex Sullivan on 10/18/2015.
 *
 * The {@link ModelDatabaseInteractor} is an interface to describe the methods available on a
 * certain database object. This should ultimately just be simple CRUD operations. The reason this
 * layer exists is because I have literally zero confidence in any ORM that I'm using, but I'm
 * also ultra super duper sick of writing SQLite so I'm giving them a shot. In order to reduce
 * the eventual (inevitable) pain of switching ORMs or switching back to SQLite, I'm using this
 * layer.
 */
public interface ModelDatabaseInteractor<T>
{
    Observable<T> fetchAll();
    Observable<T> fetchWithClause(String whereClause, String[] arguments);
    void save(T entity);
    void cascadeSave(T entity);
    void delete(T entity);
    void cascadeDelete(T entity);
}
