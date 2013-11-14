package org.hiperion.core.model.data.collector;

import org.hiperion.connector.model.DataSourceResult;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 13.04.13.
 * Time: 12:57
 */
public class PushDataRequest implements Serializable {
    private final static long serialVersionUID = 1L;

    private String dataSourceId;
    private DataSourceResult dataSourceResult;

    public PushDataRequest() {
    }

    public PushDataRequest(String dataSourceId, DataSourceResult dataSourceResult) {
        this.dataSourceId = dataSourceId;
        this.dataSourceResult = dataSourceResult;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public DataSourceResult getDataSourceResult() {
        return dataSourceResult;
    }

    public void setDataSourceResult(DataSourceResult dataSourceResult) {
        this.dataSourceResult = dataSourceResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PushDataRequest that = (PushDataRequest) o;

        if (dataSourceId != null ? !dataSourceId.equals(that.dataSourceId) : that.dataSourceId != null)
            return false;
        if (dataSourceResult != null ? !dataSourceResult.equals(that.dataSourceResult) : that.dataSourceResult != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataSourceId != null ? dataSourceId.hashCode() : 0;
        result = 31 * result + (dataSourceResult != null ? dataSourceResult.hashCode() : 0);
        return result;
    }
}
