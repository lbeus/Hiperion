package org.hiperion.core.model.data.collector;

import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.processing.CollectedDataProcessingService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 27.03.13.
 * Time: 19:19
 */
public abstract class DataCollector {

    private final String collectorId;
    private final String description;
    private final CollectedDataProcessingService collectedDataProcessingService;
    private List<SelectorBlock> selectorBlocks;

    public DataCollector(String dataCollectorId, String description,
                         CollectedDataProcessingService collectedDataProcessingService, List<SelectorBlock> selectorBlocks) {
        this.collectorId = dataCollectorId;
        this.description = description;
        this.collectedDataProcessingService = collectedDataProcessingService;
        this.selectorBlocks = selectorBlocks;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public String getDescription() {
        return description;
    }

    protected CollectedDataProcessingService getCollectedDataProcessingService() {
        return collectedDataProcessingService;
    }

    public List<SelectorBlock> getSelectorBlocks() {
        return selectorBlocks;
    }
}
