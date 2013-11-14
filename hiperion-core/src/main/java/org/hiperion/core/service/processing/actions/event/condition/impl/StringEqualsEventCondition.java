package org.hiperion.core.service.processing.actions.event.condition.impl;

import org.hiperion.connector.model.DataField;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:37
 */
public class StringEqualsEventCondition implements EventCondition {

    private final String fieldName;
    private final String value;

    public StringEqualsEventCondition(String fieldName,String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public boolean checkCondition(CollectingOutput collectingOutput) {
        DataField dataField = collectingOutput.getDataFields().get(fieldName);
        if (null == dataField) {
            return false;
        }
        return dataField.getValue().equals(value);
    }
}
