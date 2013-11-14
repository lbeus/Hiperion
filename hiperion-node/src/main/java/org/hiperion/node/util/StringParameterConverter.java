package org.hiperion.node.util;

import org.hiperion.common.exception.ParameterConversionException;

/**
 * User: iobestar
 * Date: 28.04.13.
 * Time: 11:00
 */
public class StringParameterConverter {

    public int toInteger(String parameter) throws ParameterConversionException {
        try {
            int result = Integer.parseInt(parameter);
            return result;
        } catch (NumberFormatException e) {
            throw new ParameterConversionException("Error converting to simple integer.");
        }
    }

    public double toDouble(String parameter) throws ParameterConversionException {
        try {
            double result = Double.parseDouble(parameter);
            return result;
        } catch (NumberFormatException e) {
            throw new ParameterConversionException("Error converting to simple double.");
        }
    }

    public float toFloat(String parameter) throws ParameterConversionException {
        try {
            float result = Float.parseFloat(parameter);
            return result;
        } catch (NumberFormatException e) {
            throw new ParameterConversionException("Error converting to simple float.");
        }
    }
}
