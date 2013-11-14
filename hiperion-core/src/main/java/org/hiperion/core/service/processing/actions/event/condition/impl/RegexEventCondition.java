package org.hiperion.core.service.processing.actions.event.condition.impl;

import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:39
 */
public class RegexEventCondition implements EventCondition {

    private final String fieldName;
    private final Pattern regexPattern;

    public RegexEventCondition(String fieldName, String regex) {
        this.fieldName = fieldName;
        this.regexPattern = Pattern.compile(regex);
    }

    @Override
    public boolean checkCondition(CollectingOutput collectingOutput) {
        DataField dataField = collectingOutput.getDataFields().get(fieldName);
        if (null == dataField) {
            return false;
        }

        if (dataField.getDataType() != DataType.STRING) {
            return false;
        }

        Matcher matcher = regexPattern.matcher(dataField.getValue());
        return matcher.matches();
    }
}
