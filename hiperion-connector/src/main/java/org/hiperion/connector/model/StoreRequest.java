package org.hiperion.connector.model;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 03.05.13.
 * Time: 23:39
 */
public class StoreRequest implements Serializable {

    private final static long serialVersionUID = 42L;

    private String hiperionId;
    private String streamId;
    private String dataName;
    private String dataType;
    private String dataValue;
    private long timestamp; // Time zone = UTC

    public StoreRequest() {
    }

    public String getHiperionId() {
        return hiperionId;
    }

    public void setHiperionId(String hiperionId) {
        this.hiperionId = hiperionId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreRequest that = (StoreRequest) o;

        if (timestamp != that.timestamp) return false;
        if (dataName != null ? !dataName.equals(that.dataName) : that.dataName != null) return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (dataValue != null ? !dataValue.equals(that.dataValue) : that.dataValue != null) return false;
        if (hiperionId != null ? !hiperionId.equals(that.hiperionId) : that.hiperionId != null) return false;
        if (streamId != null ? !streamId.equals(that.streamId) : that.streamId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hiperionId != null ? hiperionId.hashCode() : 0;
        result = 31 * result + (streamId != null ? streamId.hashCode() : 0);
        result = 31 * result + (dataName != null ? dataName.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (dataValue != null ? dataValue.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
