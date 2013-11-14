package org.hiperion.core.model.data.collector;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataSourceNotFound;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.collector.blocks.AcquisitionBlock;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.model.data.source.DataSource;
import org.hiperion.core.service.collecting.executor.DataSourceReadExecutor;
import org.hiperion.core.service.collecting.executor.ReadDataSourceTask;
import org.hiperion.core.service.configuration.ds.xml.DataSourceConfiguration;
import org.hiperion.core.service.processing.CollectedDataProcessingService;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.04.13.
 * Time: 14:50
 */
public class DataCollectorFactory {

    private final static Logger LOGGER = Logger.getLogger(DataCollectorFactory.class);

    private CollectedDataProcessingService collectedDataProcessingService;
    private DataSourceReadExecutor dataSourceReadExecutor;
    private DataSourceConfiguration dataSourceConfiguration;

    public DataCollectorFactory(CollectedDataProcessingService collectedDataProcessingService,
                                DataSourceReadExecutor dataSourceReadExecutor, DataSourceConfiguration dataSourceConfiguration) {
        this.collectedDataProcessingService = collectedDataProcessingService;
        this.dataSourceReadExecutor = dataSourceReadExecutor;
        this.dataSourceConfiguration = dataSourceConfiguration;
    }

    public PullDataCollector createPullDataCollector(String collectorId, String description, String cronExpression,
                                                     String acquisitionName, String[] dataSourceNames,
                                                     List<SelectorBlock> selectorBlocks) {

        Set<DataSource> dataSourceSet = new HashSet<DataSource>();
        for (String dataSourceName : dataSourceNames) {
            try {
                DataSource dataSource = dataSourceConfiguration.findById(dataSourceName);
                dataSourceSet.add(dataSource);
            } catch (DataSourceNotFound e) {
                LOGGER.error("Data source with name: " + dataSourceName + " doesn't exist.");
            } catch (HiperionException e) {
                LOGGER.error(e);
            }
        }

        AcquisitionBlock acquisitionBlock = new AcquisitionBlock(acquisitionName,dataSourceReadExecutor,dataSourceSet);

        PullDataCollector pullDataCollector = new PullDataCollector(collectorId, description, cronExpression,
                acquisitionBlock, selectorBlocks, collectedDataProcessingService);
        return pullDataCollector;
    }

    public PushDataCollector createPushDataCollector(String collectorId, String description,
                                                     List<SelectorBlock> selectorBlocks) {
        return new PushDataCollector(collectorId, description, selectorBlocks, collectedDataProcessingService);
    }
}
