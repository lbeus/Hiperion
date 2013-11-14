package org.hiperion.core.service.processing.actions.event.condition.impl;

import java.math.BigDecimal;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:35
 */
public class LessThenEventConditon extends AbstractBigDecimalEventConditon {
    public LessThenEventConditon(String fieldName, BigDecimal conditionValue) throws NumberFormatException {
        super(fieldName, conditionValue, BigDecimalCompareResult.LESS_THAN);
    }
}
