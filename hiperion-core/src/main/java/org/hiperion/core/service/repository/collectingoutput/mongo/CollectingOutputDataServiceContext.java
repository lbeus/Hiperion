package org.hiperion.core.service.repository.collectingoutput.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.repository.MongoDbConfiguration;
import org.hiperion.core.service.repository.collectingoutput.CollectingOutputDataService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 24.04.13.
 * Time: 19:23
 */
public class CollectingOutputDataServiceContext {

    private MongoDbConfiguration mongoDbConfiguration;
    private ConcurrentHashMap<String, CollectingOutputDataService> collectingOutputDataServiceCache;

    public CollectingOutputDataServiceContext(MongoDbConfiguration mongoDbConfiguration) {
        this.mongoDbConfiguration = mongoDbConfiguration;
        this.collectingOutputDataServiceCache = new ConcurrentHashMap<String, CollectingOutputDataService>();
    }

    public CollectingOutputDataService getCollectingOutputDataService(String streamId) throws HiperionException {

        if (collectingOutputDataServiceCache.contains(streamId)) {
            return collectingOutputDataServiceCache.get(streamId);
        }

        CollectingOutputDataService collectingOutputDataService = new
                MongoCollectingOutputDataService(mongoDbConfiguration.getCollectingDbCollection(streamId));
        collectingOutputDataServiceCache.put(streamId, collectingOutputDataService);
        return collectingOutputDataService;
    }
}
