package org.hiperion.core.service.collecting.manager;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.collector.PushDataCollector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 28.03.13.
 * Time: 19:11
 */
public class PushCollectingManager {

    private static final Logger LOGGER = Logger.getLogger(PushCollectingManager.class);

    private ConcurrentHashMap<String, PushDataCollector> pushDataCollectorMap;

    public PushCollectingManager() {
        this.pushDataCollectorMap = new ConcurrentHashMap<String, PushDataCollector>();
    }

    public void registerPushDataCollector(PushDataCollector pushDataCollector) {
        PushDataCollector registeredCollector = pushDataCollectorMap.get(pushDataCollector.getCollectorId());
        if (null != registeredCollector) {
            return;
        }
        pushDataCollectorMap.put(pushDataCollector.getCollectorId(), pushDataCollector);
        LOGGER.info("Push data collector " + pushDataCollector.getCollectorId() + " registered.");
        pushDataCollector.startCollector();
    }

    public void unregisterPushDataCollector(String pushDataCollectorId) {
        PushDataCollector pushDataCollector = pushDataCollectorMap.remove(pushDataCollectorId);
        if(null != pushDataCollector){
            pushDataCollector.stopCollector();
        }
        LOGGER.info("Push data collector " + pushDataCollectorId + " unregistered.");
    }

    public void runCollecting(String pushDataCollectorId, String dataSourceName,
                              DataSourceResult dataSourceResult) throws HiperionException {
        PushDataCollector pushDataCollector = pushDataCollectorMap.get(pushDataCollectorId);
        if(null == pushDataCollector){
            throw new DataCollectorNotFound();
        }
        pushDataCollector.pushData(dataSourceName, dataSourceResult);
    }

    public boolean containsDataCollector(String dataCollectorId) {
        return pushDataCollectorMap.containsKey(dataCollectorId);
    }
}
