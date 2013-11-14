package org.hiperion.node.service.datasource.http;

import org.hiperion.node.service.datasource.DataSourceServiceParameter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 30.03.13.
 * Time: 23:21
 */
public interface HttpDataSourceServiceParameter extends DataSourceServiceParameter {
    String getHostName();

    int getPort();

    Map<String, String> getDataFieldNameUriPairs();

}
