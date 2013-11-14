package org.hiperion.core.service.processing.actions.store;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.repository.collectingoutput.CollectingOutputDataService;
import org.hiperion.core.service.repository.collectingoutput.mongo.CollectingOutputDataServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: iobestar
 * Date: 19.04.13.
 * Time: 10:09
 */
public class MongoStoreService implements StoreService {

    private final static Logger LOGGER = Logger.getLogger(MongoStoreService.class);

    private CollectingOutputDataServiceContext collectingOutputDataServiceContext;

    public MongoStoreService(CollectingOutputDataServiceContext collectingOutputDataServiceContext) {
        this.collectingOutputDataServiceContext = collectingOutputDataServiceContext;
    }

    @Override
    public void store(CollectingOutput collectingOutput) throws HiperionException {
        String streamId = collectingOutput.getStreamId();
        CollectingOutputDataService collectingOutputDataService =
                collectingOutputDataServiceContext.getCollectingOutputDataService(streamId);
        collectingOutputDataService.save(collectingOutput);
    }
}
