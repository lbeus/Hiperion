package org.hiperion.connector.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 20.04.13.
 * Time: 09:31
 */
public class DataRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    private String dataSourceServiceId;
    private ParameterContext parameterContext;

    public DataRequest() {
    }

    public DataRequest(String dataSourceServiceId, ParameterContext parameterContext) {
        this.dataSourceServiceId = dataSourceServiceId;
        this.parameterContext = parameterContext;
    }

    public String getDataSourceServiceId() {
        return dataSourceServiceId;
    }

    public void setDataSourceServiceId(String dataSourceServiceId) {
        this.dataSourceServiceId = dataSourceServiceId;
    }

    public ParameterContext getParameterContext() {
        return parameterContext;
    }

    public void setParameterContext(ParameterContext parameterContext) {
        this.parameterContext = parameterContext;
    }
}
