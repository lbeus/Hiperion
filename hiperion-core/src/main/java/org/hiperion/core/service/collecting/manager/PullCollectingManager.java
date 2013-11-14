package org.hiperion.core.service.collecting.manager;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorExist;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.PullDataCollector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 28.03.13.
 * Time: 17:38
 */
public class PullCollectingManager {

    private static final Logger LOGGER = Logger.getLogger(PullCollectingManager.class);

    private final ConcurrentHashMap<String, PullDataCollector> pullDataCollectorMap;

    public PullCollectingManager() {
        this.pullDataCollectorMap = new ConcurrentHashMap<String, PullDataCollector>();
    }

    public void registerPullDataCollector(PullDataCollector pullDataCollector) {
        pullDataCollectorMap.put(pullDataCollector.getCollectorId(), pullDataCollector);
        LOGGER.info("Pull data collector " + pullDataCollector.getCollectorId() + " registered.");
    }

    public void unregisterPullDataCollector(String pullDataCollectorId)  {
        pullDataCollectorMap.remove(pullDataCollectorId);
        LOGGER.info("Pull data collector " + pullDataCollectorId + " unregistered.");
    }

    public void runCollecting(String pullDataCollectorId) throws HiperionException {
        PullDataCollector pullDataCollector = pullDataCollectorMap.get(pullDataCollectorId);
        if(null == pullDataCollector){
            throw new DataCollectorNotFound();
        }
        pullDataCollector.collectData();
    }

    public boolean containsDataCollector(String dataCollectorId) {
        return pullDataCollectorMap.containsKey(dataCollectorId);
    }
}
