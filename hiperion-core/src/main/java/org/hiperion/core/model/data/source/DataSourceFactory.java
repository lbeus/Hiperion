package org.hiperion.core.model.data.source;

import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.rmi.NodeRmiService;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 28.04.13.
 * Time: 22:21
 */
public class DataSourceFactory {

    private final NodeRmiService nodeRmiService;

    public DataSourceFactory(NodeRmiService nodeRmiService) {
        this.nodeRmiService = nodeRmiService;
    }

    public DataSource create(String dataSourceId, String nodeId, String dataSourceServiceId,
                             ParameterContext parameterContext, String description, String latitude, String longitude) {
        DataSource dataSource = new DataSource.Builder(dataSourceId, nodeId, dataSourceServiceId, parameterContext, nodeRmiService)
                .description(description).latitude(latitude).longitude(longitude).build();
        return dataSource;
    }
}
