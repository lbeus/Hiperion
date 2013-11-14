package org.hiperion.connector.model;

import org.hiperion.connector.model.DataField;

import java.io.Serializable;
import java.util.Map;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 14:47
 */
public class PushDataRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    private String dataCollectorId;
    private String dataSourceId;
    private Map<String, DataField> dataFields;

    public PushDataRequest() {
    }

    public String getDataCollectorId() {
        return dataCollectorId;
    }

    public void setDataCollectorId(String dataCollectorId) {
        this.dataCollectorId = dataCollectorId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Map<String, DataField> getDataFields() {
        return dataFields;
    }

    public void setDataFields(Map<String, DataField> dataFields) {
        this.dataFields = dataFields;
    }

    @Override
    public String toString() {
        return "PushDataRequest{" +
                "dataCollectorId='" + dataCollectorId + '\'' +
                ", dataSourceId='" + dataSourceId + '\'' +
                ", dataFields=" + dataFields +
                '}';
    }
}
