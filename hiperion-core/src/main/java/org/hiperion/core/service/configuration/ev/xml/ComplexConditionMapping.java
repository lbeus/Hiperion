package org.hiperion.core.service.configuration.ev.xml;

import org.hiperion.common.enums.EnumString;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;
import org.hiperion.core.service.processing.actions.event.condition.impl.AbstractComplexEventCondition;
import org.hiperion.core.service.processing.actions.event.condition.impl.AndEventCondition;
import org.hiperion.core.service.processing.actions.event.condition.impl.OrEventCondition;

/**
 * User: iobestar
 * Date: 11.05.13.
 * Time: 12:16
 */
public enum ComplexConditionMapping implements EnumString {
    AND_CONDITION("and-condition","Complex AND condition operator."){
        @Override
        AbstractComplexEventCondition getEventCondition() {
            return new AndEventCondition();
        }
    },
    OR_CONDITION("or-condition","Complex OR condition operator."){
        @Override
        AbstractComplexEventCondition getEventCondition() {
            return new OrEventCondition();
        }
    };

    private final String value;
    private final String description;

    private ComplexConditionMapping(String value, String description) {
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

    abstract AbstractComplexEventCondition getEventCondition();

    public static ComplexConditionMapping getComplexConditionMapping(String value){
        for(ComplexConditionMapping complexConditionMapping : ComplexConditionMapping.values()){
            if(complexConditionMapping.getValue().equals(value)){
                return complexConditionMapping;
            }
        }

        throw new IllegalArgumentException();
    }
}
