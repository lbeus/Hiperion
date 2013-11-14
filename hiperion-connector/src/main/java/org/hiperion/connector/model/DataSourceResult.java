package org.hiperion.connector.model;

import com.google.common.collect.ImmutableMap;
import org.hiperion.common.util.helpers.MapHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: iobestar
 * Date: 12.03.13.
 * Time: 19:56
 */
public class DataSourceResult implements Serializable {

    private final static long serialVersionUID = 1L;

    private Map<String, DataField> dataFields;

    public DataSourceResult() {
        this.dataFields = new HashMap<String, DataField>();
    }

    public DataSourceResult(Map<String, DataField> dataFields) {
        this.dataFields = dataFields;
    }

    public ImmutableMap<String, DataField> getDataFields() {
        return ImmutableMap.copyOf(dataFields);
    }

    public void addDataField(String fieldName, DataField dataField) {
        dataFields.put(fieldName, dataField);
    }

    public DataField getDataField(String fieldName) {
        return dataFields.get(fieldName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceResult that = (DataSourceResult) o;

        if(!MapHelper.isEquals(this.dataFields,that.dataFields)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return MapHelper.getHashCode(this.dataFields);
    }
}
