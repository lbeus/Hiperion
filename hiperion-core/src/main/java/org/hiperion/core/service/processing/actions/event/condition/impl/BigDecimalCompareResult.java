package org.hiperion.core.service.processing.actions.event.condition.impl;

/**
 * User: iobestar
 * Date: 09.05.13.
 * Time: 20:43
 */
public enum BigDecimalCompareResult implements ResultComparator {
    GREATER_THAN{
        @Override
        public boolean compareResult(int result) {
            if(1 == result){
                return true;
            }
            return false;
        }
    },
    LESS_THAN{
        @Override
        public boolean compareResult(int result) {
            if(-1 == result){
                return true;
            }
            return false;
        }
    },
    EQUALS{
        @Override
        public boolean compareResult(int result) {
            if(0 == result){
                return true;
            }
            return false;
        }
    };

    @Override
    abstract public  boolean compareResult(int result);
}
