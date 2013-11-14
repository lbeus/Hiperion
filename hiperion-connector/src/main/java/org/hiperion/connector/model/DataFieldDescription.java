package org.hiperion.connector.model;

import org.hiperion.connector.model.enums.DataType;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 29.04.13.
 * Time: 20:51
 */
public class DataFieldDescription implements Serializable {

    private final static long serialVersionUID = 1L;

    private String dataFieldName;
    private DataType dataType;

    public DataFieldDescription() {
    }

    public DataFieldDescription(String dataFieldName, DataType dataType) {
        this.dataFieldName = dataFieldName;
        this.dataType = dataType;
    }

    public String getDataFieldName() {
        return dataFieldName;
    }

    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
