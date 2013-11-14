package org.hiperion.core.model.data.collector;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.blocks.AcquisitionBlock;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.processing.CollectedDataProcessingService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 12.03.13.
 * Time: 16:45
 */
public class PullDataCollector extends DataCollector {

    private final String cronExpression;
    private final AcquisitionBlock acquisitionBlock;

    public PullDataCollector(String collectorId, String description, String cronExpression,
                             AcquisitionBlock acquisitionBlock,
                             List<SelectorBlock> selectorBlocks,
                             CollectedDataProcessingService collectedDataProcessingService) {
        super(collectorId, description, collectedDataProcessingService,selectorBlocks);
        this.cronExpression = cronExpression;
        this.acquisitionBlock = acquisitionBlock;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void collectData() throws HiperionException {
        DataCollectorCache dataCollectorCache = DataCollectorCache.newInstance();
        acquisitionBlock.executeBlock(getCollectorId(), dataCollectorCache);
        for (SelectorBlock selectorBlock : getSelectorBlocks()) {
            selectorBlock.executeBlock(getCollectorId(), dataCollectorCache);
        }
        getCollectedDataProcessingService().processOutputs(dataCollectorCache.getCollectingOutputs());
    }
}
