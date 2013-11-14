package org.hiperion.core.service.processing.actions.archive;

import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.ProcessingAction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * User: iobestar
 * Date: 08.05.13.
 * Time: 20:44
 */
@Component
public class ArchiveProcessingAction implements ProcessingAction {
    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public void processCollectingOutput(CollectingOutput collectingOutput) {
        System.out.println("Ovdje treba pozvati komponentu za arhiviranje. Decki, preddiplomski.");
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
