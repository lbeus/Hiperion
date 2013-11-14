package org.hiperion.core.service.processing.actions.event.condition.impl;

import java.math.BigDecimal;

/**
 * User: iobestar
 * Date: 09.05.13.
 * Time: 20:49
 */
public class EqualsEventConditon extends AbstractBigDecimalEventConditon {
    public EqualsEventConditon(String fieldName, BigDecimal conditionValue) throws NumberFormatException {
        super(fieldName, conditionValue, BigDecimalCompareResult.EQUALS);
    }
}
