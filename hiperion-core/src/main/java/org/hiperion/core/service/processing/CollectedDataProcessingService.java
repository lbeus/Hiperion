package org.hiperion.core.service.processing;

import org.apache.log4j.Logger;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;
import org.hiperion.core.service.collecting.CollectingOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: iobestar
 * Date: 04.05.13.
 * Time: 18:02
 */
public class CollectedDataProcessingService {

    private final static Logger LOGGER = Logger.getLogger(CollectedDataProcessingService.class);

    private final Map<String,Integer> processingMapping;
    private final int[] processorWeightIndex;

    private final QueueItemProcessor<CollectingOutput> dispatchProcessor;
    private final CollectingOutputQueueProcessor[] outputQueueProcessors;

    public CollectedDataProcessingService(final CollectingOutputQueueProcessor[] outputQueueProcessors) {
        this.processingMapping = new HashMap<String, Integer>();
        this.processorWeightIndex = new int[outputQueueProcessors.length];

        this.outputQueueProcessors = outputQueueProcessors;
        this.dispatchProcessor = new QueueItemProcessor<CollectingOutput>(new ItemProcessor<CollectingOutput>() {
            @Override
            public void process(CollectingOutput collectingOutput) {
                Integer processorIndex = calculateProcessorIndex(collectingOutput.getStreamId());
                outputQueueProcessors[processorIndex].submitForProcessing(collectingOutput);
            }
        }, new LinkedBlockingQueue<CollectingOutput>());
    }

    public void startService() {

        this.dispatchProcessor.start();
        for (CollectingOutputQueueProcessor collectingOutputQueueProcessor : outputQueueProcessors) {
            collectingOutputQueueProcessor.startProcessor();
        }
    }

    private int calculateProcessorIndex(String streamId){
        Integer processorIndex = processingMapping.get(streamId);
        if(null != processorIndex){
            return processorIndex;
        }

        int initialWeight = processorWeightIndex[0];
        for(int i = 1; i< processorWeightIndex.length; i++){
            if(processorWeightIndex[i] < initialWeight){
                processorWeightIndex[i]++;
                processingMapping.put(streamId,i);
                return i;
            }
        }

        // all weight indices are equal
        processorWeightIndex[0]++;
        processingMapping.put(streamId,0);
        return 0;
    }

    public void stopService() {
        this.dispatchProcessor.stop();
        for (CollectingOutputQueueProcessor collectingOutputQueueProcessor : outputQueueProcessors) {
            collectingOutputQueueProcessor.stopProcessor();
        }
    }

    public void processOutputs(List<CollectingOutput> collectingOutputs) {
        for (CollectingOutput collectingOutput : collectingOutputs) {
            dispatchProcessor.queue(collectingOutput);
        }
    }
}
