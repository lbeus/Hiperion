package org.hiperion.core.service.processing;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;

/**
 * User: iobestar
 * Date: 19.03.13.
 * Time: 19:51
 */
public interface ProcessingAction {

    void init();

    void processCollectingOutput(CollectingOutput collectingOutput) throws HiperionException;

    void dispose();
}
