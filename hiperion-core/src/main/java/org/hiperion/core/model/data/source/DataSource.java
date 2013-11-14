package org.hiperion.core.model.data.source;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.rmi.NodeRmiService;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 12.03.13.
 * Time: 19:46
 */

public class DataSource {

    private static final Logger LOGGER = Logger.getLogger(DataSource.class);

    private final String dataSourceId;
    private final String nodeId;
    private final String dataSourceServiceId;
    private final ParameterContext parameterContext;
    private final NodeRmiService nodeRmiService;

    private final String description;
    private final String latitude;
    private final String longitude;

    public static class Builder {
        //Required
        private final String dataSourceId;
        private final String nodeId;
        private final String dataSourceServiceId;
        private final ParameterContext parameterContext;
        private final NodeRmiService nodeRmiService;

        //Optional
        private String description = "";
        private String latitude = "";
        private String longitude = "";

        public Builder(String dataSourceId, String nodeId, String dataSourceServiceId, ParameterContext parameterContext,
                       NodeRmiService nodeRmiService) {
            this.dataSourceId = dataSourceId;
            this.nodeId = nodeId;
            this.parameterContext = parameterContext;
            this.dataSourceServiceId = dataSourceServiceId;
            this.nodeRmiService = nodeRmiService;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder latitude(String val) {
            latitude = val;
            return this;
        }

        public Builder longitude(String val) {
            longitude = val;
            return this;
        }

        public DataSource build() {
            return new DataSource(this);
        }
    }

    private DataSource(Builder builder) {
        this.dataSourceId = builder.dataSourceId;
        this.nodeId = builder.nodeId;
        this.dataSourceServiceId = builder.dataSourceServiceId;
        this.parameterContext = builder.parameterContext;
        this.nodeRmiService = builder.nodeRmiService;
        this.description = builder.description;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public DataSourceResult getDataSourceResult() throws HiperionException {
        try {
            Collection<DataField> dataFields = nodeRmiService.getData(nodeId, dataSourceServiceId, parameterContext);
            DataSourceResult dataSourceResult = new DataSourceResult();
            for (DataField dataField : dataFields) {
                dataSourceResult.addDataField(dataField.getName(), dataField);
            }
            return dataSourceResult;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new HiperionException(e);
        }
    }
}
