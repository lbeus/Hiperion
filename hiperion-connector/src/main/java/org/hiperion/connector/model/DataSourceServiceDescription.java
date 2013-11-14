package org.hiperion.connector.model;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 29.04.13.
 * Time: 20:40
 */
public class DataSourceServiceDescription implements Serializable {

    private final static long serialVersionUID = 1L;

    private String dataSourceServiceId;
    private String dataSourceServiceDescription;

    public DataSourceServiceDescription() {
    }

    public DataSourceServiceDescription(String dataSourceServiceId, String dataSourceServiceDescription) {
        this.dataSourceServiceId = dataSourceServiceId;
        this.dataSourceServiceDescription = dataSourceServiceDescription;
    }

    public String getDataSourceServiceId() {
        return dataSourceServiceId;
    }

    public void setDataSourceServiceId(String dataSourceServiceId) {
        this.dataSourceServiceId = dataSourceServiceId;
    }

    public String getDataSourceServiceDescription() {
        return dataSourceServiceDescription;
    }

    public void setDataSourceServiceDescription(String dataSourceServiceDescription) {
        this.dataSourceServiceDescription = dataSourceServiceDescription;
    }
}
