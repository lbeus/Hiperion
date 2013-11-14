package org.hiperion.core.service.processing.actions.event.publisher.comet;

import org.apache.log4j.Logger;
import org.hiperion.core.service.processing.actions.event.publisher.EventPublisher;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;
import org.hiperion.core.service.processing.util.CometPublisher;


/**
 * User: iobestar
 * Date: 23.05.13.
 * Time: 19:11
 */
public class CometEventPublisher implements EventPublisher {

    private final static Logger LOGGER = Logger.getLogger(CometEventPublisher.class);

    private final static String CHANNEL_NAME = "/events";

    private CometPublisher<PublishEvent> cometPublisher;

    public CometEventPublisher(CometPublisher<PublishEvent> cometPublisher) {
        this.cometPublisher = cometPublisher;
    }

    @Override
    public void publishEvent(PublishEvent publishEvent) {
        cometPublisher.publishMessage(publishEvent,CHANNEL_NAME);
    }
}
