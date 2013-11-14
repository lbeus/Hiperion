package org.hiperion.core.service.collecting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorNotDeployed;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.model.data.collector.PullDataCollector;
import org.hiperion.core.model.data.collector.PushDataCollector;
import org.hiperion.core.service.collecting.manager.PullCollectingManager;
import org.hiperion.core.service.collecting.manager.PushCollectingManager;
import org.hiperion.core.service.collecting.manager.ScheduledPullCollectingManager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 20.03.13.
 * Time: 19:11
 */
public class CollectingService {

    private final static Logger LOGGER = Logger.getLogger(CollectingService.class);

    private final PullCollectingManager pullCollectingManager;
    private final ScheduledPullCollectingManager scheduledPullCollectingManager;
    private final PushCollectingManager pushCollectingManager;

    private final Set<String> registeredCollectors;

    public CollectingService(PullCollectingManager pullCollectingManager,
                             ScheduledPullCollectingManager scheduledPullCollectingManager,
                             PushCollectingManager pushCollectingManager) {
        this.pullCollectingManager = pullCollectingManager;
        this.scheduledPullCollectingManager = scheduledPullCollectingManager;
        this.pushCollectingManager = pushCollectingManager;
        this.registeredCollectors = Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }

    public void collectData(String dataCollectorId, String dataSourceName,
                            DataSourceResult dataSourceResult) throws HiperionException {
        LOGGER.info("Request for push collecting: " + dataCollectorId);
        if(!pushCollectingManager.containsDataCollector(dataCollectorId)){
            throw new DataCollectorNotDeployed();
        }
        pushCollectingManager.runCollecting(dataCollectorId,dataSourceName, dataSourceResult);
    }

    public void collectData(String dataCollectorId) throws HiperionException{
        LOGGER.info("Request for pull collecting: " + dataCollectorId);
        if(!pullCollectingManager.containsDataCollector(dataCollectorId)){
            throw new DataCollectorNotDeployed();
        }
        pullCollectingManager.runCollecting(dataCollectorId);
    }

    public void deployCollector(DataCollector dataCollector) throws HiperionException {
        LOGGER.info("Deploying data collector: " + dataCollector.getCollectorId());

        if (dataCollector instanceof PullDataCollector) {
            PullDataCollector pullDataCollector = (PullDataCollector) dataCollector;
            if (!pullDataCollector.getCronExpression().equals("")) {
                scheduledPullCollectingManager.schedulePullCollecting(pullDataCollector);
                return;
            }
            pullCollectingManager.registerPullDataCollector((PullDataCollector) dataCollector);
            registeredCollectors.add(dataCollector.getCollectorId());
            return;
        }

        if (dataCollector instanceof PushDataCollector) {
            PushDataCollector pushDataCollector = (PushDataCollector) dataCollector;
            pushCollectingManager.registerPushDataCollector(pushDataCollector);
            registeredCollectors.add(dataCollector.getCollectorId());
        }
    }

    public void undeployCollector(String dataCollectorId) throws HiperionException {
        LOGGER.info("Undeploying data collector: " + dataCollectorId);
        if (pullCollectingManager.containsDataCollector(dataCollectorId)) {
            pullCollectingManager.unregisterPullDataCollector(dataCollectorId);
            registeredCollectors.remove(dataCollectorId);
            return;
        }

        if (pushCollectingManager.containsDataCollector(dataCollectorId)) {
            pushCollectingManager.unregisterPushDataCollector(dataCollectorId);
            registeredCollectors.remove(dataCollectorId);
            return;
        }

        if (scheduledPullCollectingManager.containsDataCollector(dataCollectorId)) {
            scheduledPullCollectingManager.unschedulePullCollecting(dataCollectorId);
            return;
        }
    }

    public boolean isDeployed(String collectorName){
        if(pullCollectingManager.containsDataCollector(collectorName)){
            return true;
        }

        if(pushCollectingManager.containsDataCollector(collectorName)){
            return true;
        }

        if(scheduledPullCollectingManager.containsDataCollector(collectorName)){
            return true;
        }

        return false;
    }

    public ImmutableSet<String> getDeployedCollectors(){
        return ImmutableSet.copyOf(registeredCollectors);
    }

}
