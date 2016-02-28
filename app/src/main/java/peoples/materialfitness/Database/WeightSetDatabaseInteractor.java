package peoples.materialfitness.Database;

import java.util.List;

import rx.Observable;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WeightSetDatabaseInteractor implements ModelDatabaseInteractor<WeightSet>
{
    @Override
    public Observable<WeightSet> fetchAll()
    {
        return Observable.empty();
//        return Observable.from(WeightSet.listAll(WeightSet.class));
    }

    @Override
    public Observable<WeightSet> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.empty();
//        return Observable.from(WeightSet.find(WeightSet.class, whereClause, arguments));
    }

    @Override
    public void save(WeightSet entity)
    {
//        entity.save();
    }

    @Override
    public void delete(WeightSet entity)
    {
//        entity.delete();
    }

    @Override
    public void cascadeSave(WeightSet entity)
    {

    }

    @Override
    public void cascadeDelete(WeightSet entity)
    {

    }
}
