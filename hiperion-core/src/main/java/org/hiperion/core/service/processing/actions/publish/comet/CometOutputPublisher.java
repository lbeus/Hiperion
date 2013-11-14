package org.hiperion.core.service.processing.actions.publish.comet;

import org.apache.log4j.Logger;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;
import org.hiperion.core.service.processing.actions.publish.OutputPublisher;
import org.hiperion.core.service.processing.util.CometPublisher;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 23:01
 */
public class CometOutputPublisher implements OutputPublisher {

    private final static Logger LOGGER = Logger.getLogger(CometOutputPublisher.class);

    private final static String CHANNEL_NAME = "/data";

    private CometPublisher<CollectingOutput> cometPublisher;

    public CometOutputPublisher(CometPublisher<CollectingOutput> cometPublisher) {
        this.cometPublisher = cometPublisher;
    }

    @Override
    public void publishOutput(CollectingOutput collectingOutput) {
        String channelName = CHANNEL_NAME + "/" + collectingOutput.getStreamId();
        cometPublisher.publishMessage(collectingOutput,channelName);
    }
}
