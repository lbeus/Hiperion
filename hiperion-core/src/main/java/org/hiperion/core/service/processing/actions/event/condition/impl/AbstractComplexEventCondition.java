package org.hiperion.core.service.processing.actions.event.condition.impl;

import com.google.common.collect.ImmutableList;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 11.05.13.
 * Time: 12:24
 */
public abstract class AbstractComplexEventCondition implements EventCondition {

    private final List<EventCondition> eventConditions;

    public AbstractComplexEventCondition() {
        this.eventConditions = new LinkedList<EventCondition>();
    }

    public abstract boolean checkCondition(CollectingOutput collectingOutput);

    public void addConditionOperator(EventCondition eventCondition) {
        eventConditions.add(eventCondition);
    }

    public ImmutableList<EventCondition> getEventConditions(){
        return ImmutableList.copyOf(eventConditions);
    }
}
