package org.hiperion.core.service.processing.actions.event.condition.impl;

import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:27
 */
public class AndEventCondition extends AbstractComplexEventCondition {

    @Override
    public boolean checkCondition(CollectingOutput collectingOutput) {
        for (EventCondition eventCondition : getEventConditions()) {
            if (!eventCondition.checkCondition(collectingOutput)) {
                return false;
            }
        }
        return true;
    }
}
