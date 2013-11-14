package org.hiperion.connector.model;

import org.hiperion.connector.model.enums.DataType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 14.03.13.
 * Time: 11:45
 */
public class DataField implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private DataType dataType;
    private String value;
    private long timestamp;

    public DataField(String name, DataType dataType, String value) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.timestamp = new DateTime(DateTimeZone.UTC).getMillis();
    }

    public DataField(String name, DataType dataType, String value, long timestamp) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.timestamp = timestamp;
    }

    public DataField() {
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataField dataField = (DataField) o;

        if (timestamp != dataField.timestamp) return false;
        if (dataType != dataField.dataType) return false;
        if (name != null ? !name.equals(dataField.name) : dataField.name != null) return false;
        if (value != null ? !value.equals(dataField.value) : dataField.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
