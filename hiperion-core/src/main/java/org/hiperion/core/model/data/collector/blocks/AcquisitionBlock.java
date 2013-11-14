package org.hiperion.core.model.data.collector.blocks;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.collector.DataCollectorCache;
import org.hiperion.core.model.data.source.DataSource;
import org.hiperion.core.service.collecting.executor.DataSourceReadExecutor;
import org.hiperion.core.service.collecting.executor.ReadDataSourceTask;
import org.hiperion.core.util.Config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.03.13.
 * Time: 11:42
 */
public class AcquisitionBlock implements CollectingBlock {

    private final static Logger LOGGER = Logger.getLogger(AcquisitionBlock.class);

    private static final int WAIT_INTERVAL_TIME = 50; // ms

    private String acquisitionName;
    private final Set<DataSource> dataSources;
    private final DataSourceReadExecutor dataSourceReadExecutor;

    public AcquisitionBlock(String acquisitionName, DataSourceReadExecutor dataSourceReadExecutor,
                            Set<DataSource> dataSources) {
        this.acquisitionName = acquisitionName;
        this.dataSources = dataSources;
        this.dataSourceReadExecutor = dataSourceReadExecutor;
    }

    public String getAcquisitionName() {
        return acquisitionName;
    }

    @Override
    public void executeBlock(String collectorId, DataCollectorCache dataCollectorCache) throws HiperionException {
        Map<String, Future<DataSourceResult>> collectingResults = new HashMap<String, Future<DataSourceResult>>();
        for (DataSource dataSource : dataSources) {
            collectingResults.put(dataSource.getDataSourceId(),
                    dataSourceReadExecutor.executeReading(new ReadDataSourceTask(dataSource)));
        }

        int numberOfDataSources = dataSources.size();
        Set<String> doneDataSources = new HashSet<String>();
        long waitTime = Config.DEFAULT_DATA_SOURCE_READ_TIME;

        try {
            while (true) {
                Thread.sleep(WAIT_INTERVAL_TIME);
                for (String dataSourceName : collectingResults.keySet()) {
                    Future<DataSourceResult> dataSourceResultFuture = collectingResults.get(dataSourceName);
                    if (dataSourceResultFuture.isDone()) {
                        DataSourceResult dataSourceResult = dataSourceResultFuture.get();
                        if (null != dataSourceResult) {
                            dataCollectorCache.addToCache(dataSourceName, dataSourceResult);
                        }
                        doneDataSources.add(dataSourceName);
                    }
                }

                waitTime -= WAIT_INTERVAL_TIME;
                if(waitTime <= 0){
                    if(doneDataSources.size() == numberOfDataSources){
                        break;
                    }
                    LOGGER.warn("Not all data sources readout. Data source read timeout.");
                }
                if(doneDataSources.size() == numberOfDataSources){
                    break;
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } catch (ExecutionException e) {
            LOGGER.error(e);
        }
    }

}
