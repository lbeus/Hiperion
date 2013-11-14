package org.hiperion.core.service.processing.actions.publish;

import org.hiperion.core.service.collecting.CollectingOutput;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 23:00
 */
public interface OutputPublisher {
    void publishOutput(CollectingOutput collectingOutput);
}
