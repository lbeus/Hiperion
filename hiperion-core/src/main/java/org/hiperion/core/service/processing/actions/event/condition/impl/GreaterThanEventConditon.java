package org.hiperion.core.service.processing.actions.event.condition.impl;

import java.math.BigDecimal;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:34
 */
public class GreaterThanEventConditon extends AbstractBigDecimalEventConditon {

    public GreaterThanEventConditon(String fieldName, BigDecimal conditionValue) throws NumberFormatException {
        super(fieldName,conditionValue, BigDecimalCompareResult.GREATER_THAN);

    }
}
