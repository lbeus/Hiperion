package org.hiperion.core.model.data.collector;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.processing.CollectedDataProcessingService;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 27.03.13.
 * Time: 19:22
 */
public class PushDataCollector extends DataCollector implements ItemProcessor<PushDataRequest> {

    private static final Logger LOGGER = Logger.getLogger(PushDataCollector.class);

    private QueueItemProcessor<PushDataRequest> queueItemProcessor;

    public PushDataCollector(String dataCollectorId, String description,
                             List<SelectorBlock> selectorBlocks,
                             CollectedDataProcessingService collectedDataQueueProcessor) {
        super(dataCollectorId, description, collectedDataQueueProcessor, selectorBlocks);
        this.queueItemProcessor = new QueueItemProcessor<PushDataRequest>(
                this, new LinkedBlockingDeque<PushDataRequest>());
    }

    public void startCollector() {
        queueItemProcessor.start();
        LOGGER.info("Push data collector " + getCollectorId() + " started.");
    }

    public void stopCollector() {
        queueItemProcessor.stop();
        LOGGER.info("Push data collector " + getCollectorId() + " stopped.");
    }

    public void pushData(String dataSourceId, DataSourceResult dataSourceResult) {
        PushDataRequest pushDataRequest = new PushDataRequest(dataSourceId, dataSourceResult);
        queueItemProcessor.queue(pushDataRequest);
    }

    @Override
    public void process(PushDataRequest pushDataRequest) {
        DataCollectorCache dataCollectorCache = DataCollectorCache.newInstance();
        dataCollectorCache.addToCache(pushDataRequest.getDataSourceId(), pushDataRequest.getDataSourceResult());
        for (SelectorBlock selectorBlock : getSelectorBlocks()) {
            try {
                selectorBlock.executeBlock(getCollectorId(), dataCollectorCache);
            } catch (HiperionException e) {
                LOGGER.error(e);
            }
        }
        getCollectedDataProcessingService().processOutputs(dataCollectorCache.getCollectingOutputs());
    }
}
