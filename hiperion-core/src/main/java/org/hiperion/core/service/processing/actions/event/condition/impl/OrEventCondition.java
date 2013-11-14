package org.hiperion.core.service.processing.actions.event.condition.impl;

import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:31
 */
public class OrEventCondition extends AbstractComplexEventCondition {

    @Override
    public boolean checkCondition(CollectingOutput collectingOutput) {
        for (EventCondition eventCondition : getEventConditions()) {
            if (eventCondition.checkCondition(collectingOutput)) {
                return true;
            }
        }
        return false;
    }
}
