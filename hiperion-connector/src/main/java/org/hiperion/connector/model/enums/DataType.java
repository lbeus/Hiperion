package org.hiperion.connector.model.enums;

import org.hiperion.common.enums.EnumInt;

/**
 * User: iobestar
 * Date: 12.03.13.
 * Time: 20:00
 */
public enum DataType implements EnumInt {

    STRING(0, "Raw string data type."),
    INTEGER(1, "Integer data type"),
    DOUBLE(2, "Double data type."),
    BINARY_PNG(3, "Picture PNG type."),
    BINARY_JPG(4, "Picture JPG type."),
    JSON(5, "Raw JSON data type"),
    RAW_DATA(6, "Raw data type."),
    UNDEFINED(7, "Undefined data type.");

    private final int value;

    private final String description;

    private DataType(int value, String description) {
        this.value = value;
        this.description = description;
    }


    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static DataType getDataType(int value) {
        for (DataType dataType : DataType.values()) {
            if (dataType.getValue() == value) {
                return dataType;
            }
        }
        return DataType.UNDEFINED;
    }
}
