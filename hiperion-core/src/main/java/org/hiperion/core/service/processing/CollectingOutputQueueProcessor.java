package org.hiperion.core.service.processing;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.exception.ProcessingActionNotFound;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.config.StreamProcessingConfig;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 25.03.13.
 * Time: 17:58
 */
public class CollectingOutputQueueProcessor {

    private static final Logger LOGGER = Logger.getLogger(CollectingOutputQueueProcessor.class);

    private final QueueItemProcessor<CollectingOutput> queueProcessor;
    private final StreamProcessingConfig streamProcessingConfig;
    private final ProcessingActionContextProxy processingActionContextProxy;

    public CollectingOutputQueueProcessor(StreamProcessingConfig streamProcessingConfig,
                                          ProcessingActionContextProxy processingActionContextProxy) {
        this.queueProcessor = new QueueItemProcessor<CollectingOutput>(new ItemProcessor<CollectingOutput>() {
            @Override
            public void process(CollectingOutput item) {
                processCollectingOutput(item);
            }
        }, new LinkedBlockingDeque<CollectingOutput>());
        this.streamProcessingConfig = streamProcessingConfig;
        this.processingActionContextProxy = processingActionContextProxy;
    }

    public void startProcessor() {
        queueProcessor.start();
    }

    public void stopProcessor() {
        queueProcessor.stop();
    }

    public void processCollectingOutput(CollectingOutput collectingOutput) {
        for (ProcessingActionType processingActionType : ProcessingActionType.values()) {
            String streamId = collectingOutput.getStreamId();
            if (streamProcessingConfig.isProcessingEnabled(streamId, processingActionType)) {
                try {
                    ProcessingAction processingAction = processingActionContextProxy.getProcessingAction(
                            processingActionType.getProcessingActionType());
                    processingAction.processCollectingOutput(collectingOutput);
                } catch (ProcessingActionNotFound e) {
                    LOGGER.error(e);
                } catch (HiperionException e) {
                    LOGGER.error(e);
                }
            }
        }
    }

    public void submitForProcessing(CollectingOutput collectingOutput) {
        queueProcessor.queue(collectingOutput);
    }
}
