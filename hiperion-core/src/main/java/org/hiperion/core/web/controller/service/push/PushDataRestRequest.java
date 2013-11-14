package org.hiperion.core.web.controller.service.push;

import org.hiperion.common.util.helpers.MapHelper;
import org.hiperion.connector.model.DataField;

import java.io.Serializable;
import java.util.Map;

/**
 * User: iobestar
 * Date: 21.05.13.
 * Time: 21:26
 */
public class PushDataRestRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    private String dataCollectorId;
    private String dataSourceId;
    private Map<String,DataField> dataFields;

    public PushDataRestRequest() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PushDataRestRequest that = (PushDataRestRequest) o;

        if (dataCollectorId != null ? !dataCollectorId.equals(that.dataCollectorId) : that.dataCollectorId != null)
            return false;
        if (dataSourceId != null ? !dataSourceId.equals(that.dataSourceId) : that.dataSourceId != null) return false;
        if (!MapHelper.isEquals(this.dataFields,that.dataFields)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataCollectorId != null ? dataCollectorId.hashCode() : 0;
        result = 31 * result + (dataSourceId != null ? dataSourceId.hashCode() : 0);
        result = 31 * result + MapHelper.getHashCode(this.dataFields);
        return result;
    }
}
