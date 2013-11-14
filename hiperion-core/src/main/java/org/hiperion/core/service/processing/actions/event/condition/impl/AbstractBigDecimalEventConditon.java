package org.hiperion.core.service.processing.actions.event.condition.impl;

import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

import java.math.BigDecimal;

/**
 * User: iobestar
 * Date: 09.05.13.
 * Time: 20:30
 */
public abstract class AbstractBigDecimalEventConditon implements EventCondition {

    private final String fieldName;
    private final BigDecimal conditionValue;
    private final ResultComparator resultComparator;

    public AbstractBigDecimalEventConditon(String fieldName, BigDecimal conditionValue, ResultComparator resultComparator) throws NumberFormatException {
        this.fieldName = fieldName;
        this.conditionValue = conditionValue;
        this.resultComparator = resultComparator;
    }

    @Override
    public boolean checkCondition(CollectingOutput collectingOutput) {

        DataField dataField = collectingOutput.getDataFields().get(fieldName);
        if( null == dataField){
            return false;
        }

        if(dataField.getDataType() != DataType.DOUBLE &&
                dataField.getDataType() != DataType.INTEGER){
            return false;
        }

        try{
            BigDecimal fieldValue = new BigDecimal(dataField.getValue());
            int result = fieldValue.compareTo(conditionValue);
            return resultComparator.compareResult(result);
        } catch (NumberFormatException e){
            return false;
        }
    }
}
