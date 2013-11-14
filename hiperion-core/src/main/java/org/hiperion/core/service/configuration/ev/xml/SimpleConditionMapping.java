package org.hiperion.core.service.configuration.ev.xml;

import org.hiperion.common.enums.EnumString;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;
import org.hiperion.core.service.processing.actions.event.condition.impl.*;

import java.math.BigDecimal;

/**
 * User: iobestar
 * Date: 11.05.13.
 * Time: 12:01
 */
public enum SimpleConditionMapping implements EnumString{
    GREATER_THAN("greater-than","Greater than big decimal operator."){
        @Override
        EventCondition getEventCondition(String fieldName, String conditionValue) {
            BigDecimal convertedConditionValue = new BigDecimal(conditionValue);
            return new GreaterThanEventConditon(fieldName,convertedConditionValue);
        }
    },
    LESS_THAN("less-than", "Less than big decimal operator."){
        @Override
        EventCondition getEventCondition(String fieldName, String conditionValue) {
            BigDecimal convertedConditionValue = new BigDecimal(conditionValue);
            return new LessThenEventConditon(fieldName,convertedConditionValue);
        }
    },
    EQUALS("equals", "Equals big decimal operator."){
        @Override
        EventCondition getEventCondition(String fieldName, String conditionValue) throws NumberFormatException {
            BigDecimal convertedConditionValue = new BigDecimal(conditionValue);
            return new EqualsEventConditon(fieldName,convertedConditionValue);
        }
    },
    REGEX_MATCH("regex-match","Regex pattern match operator."){
        @Override
        EventCondition getEventCondition(String fieldName, String conditionValue) throws NumberFormatException {
            return new RegexEventCondition(fieldName,conditionValue);
        }
    },
    STRING_EQUALS("string-equals", "String equals operator."){
        @Override
        EventCondition getEventCondition(String fieldName, String conditionValue) throws NumberFormatException {
            return new StringEqualsEventCondition(fieldName,conditionValue);
        }
    };

    private final String value;
    private final String description;

    private SimpleConditionMapping(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    abstract EventCondition getEventCondition(String fieldName, String conditionValue) throws NumberFormatException;

    public static SimpleConditionMapping getSimpleConditionMapping(String value){
        for(SimpleConditionMapping simpleConditionMapping : SimpleConditionMapping.values()){
            if(simpleConditionMapping.getValue().equals(value)){
                return simpleConditionMapping;
            }
        }

        throw new IllegalArgumentException();
    }
}
