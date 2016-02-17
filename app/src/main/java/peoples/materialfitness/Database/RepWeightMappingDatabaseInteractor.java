package peoples.materialfitness.Database;

import rx.Observable;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class RepWeightMappingDatabaseInteractor implements ModelDatabaseInteractor<RepWeightMapping>
{
    @Override
    public Observable<RepWeightMapping> fetchAll()
    {
        return Observable.from(RepWeightMapping.listAll(RepWeightMapping.class));
    }

    @Override
    public Observable<RepWeightMapping> fetchWithClause(String whereClause, String[] arguments)
    {
        return Observable.from(RepWeightMapping.find(RepWeightMapping.class, whereClause, arguments));
    }

    @Override
    public void save(RepWeightMapping entity)
    {
        entity.save();
    }

    @Override
    public void delete(RepWeightMapping entity)
    {
        entity.delete();
    }
}
