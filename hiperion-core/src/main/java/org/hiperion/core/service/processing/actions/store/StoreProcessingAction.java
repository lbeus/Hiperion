package org.hiperion.core.service.processing.actions.store;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.ProcessingAction;
import org.hiperion.core.service.processing.ProcessingActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 19.04.13.
 * Time: 10:03
 */
@Component
public class StoreProcessingAction implements ProcessingAction {

    private final static Logger LOGGER = Logger.getLogger(StoreProcessingAction.class);

    private final StoreService storeService;

    @Autowired
    public StoreProcessingAction(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public void processCollectingOutput(CollectingOutput collectingOutput) {
        try {
            storeService.store(collectingOutput);
        } catch (HiperionException e) {
            LOGGER.error(e);
        }
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
