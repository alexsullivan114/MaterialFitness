package peoples.materialfitness.Model;

import rx.Observable;

/**
 * Created by Alex Sullivan on 10/18/2015.
 *
 * The {@link ModelDatabaseInteractor} is an abstract class to describe the methods available on a
 * certain database object. This should ultimately just be simple CRUD operations. The reason this
 * layer exists is because I have literally zero confidence in any ORM that I'm using, but I'm
 * also ultra super duper sick of writing SQLite so I'm giving them a shot. In order to reduce
 * the eventual (inevitable) pain of switching ORMs or switching back to SQLite, I'm using this
 * layer.
 */
public abstract class ModelDatabaseInteractor<T>
{
    public static final int INVALID_ID = -1;

    public abstract Observable<T> fetchWithArguments(final String whereClause,
                                     final String[] args,
                                     final String groupBy,
                                     final String[] columns,
                                     final String having,
                                     final String orderBy,
                                     final String limit);
    public abstract Observable<Long> save(T entity);
    public abstract Observable<Long> cascadeSave(T entity);
    public abstract void delete(T entity);
    public abstract void cascadeDelete(T entity);

    public Observable<T> fetchAll()
    {
        return fetchWithClause(null, null);
    }

    public Observable<T> fetchWithClause(String whereClause, String[] arguments)
    {
        return fetchWithArguments(whereClause, arguments, null, null, null, null, null);
    }

    public enum Ordering
    {
        DESC,
        ASC;
    }
}
