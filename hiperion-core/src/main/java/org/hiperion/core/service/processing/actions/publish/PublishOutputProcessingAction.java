package org.hiperion.core.service.processing.actions.publish;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.ProcessingAction;
import org.hiperion.core.service.processing.actions.publish.comet.CometOutputPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 22:58
 */
@Component
public class PublishOutputProcessingAction implements ProcessingAction {

    private static final Logger LOGGER = Logger.getLogger(PublishOutputProcessingAction.class);

    private final OutputPublisher outputPublisher;

    @Autowired
    public PublishOutputProcessingAction(OutputPublisher outputPublisher) {
        this.outputPublisher = outputPublisher;
    }

    @PostConstruct
    @Override
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processCollectingOutput(CollectingOutput collectingOutput) throws HiperionException {
        outputPublisher.publishOutput(collectingOutput);
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
