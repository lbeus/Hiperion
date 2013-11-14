package org.hiperion.core.service.processing.actions.event.condition;

import org.hiperion.core.service.collecting.CollectingOutput;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 19:41
 */
public interface EventCondition {

    boolean checkCondition(CollectingOutput collectingOutput);
}
