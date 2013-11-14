package org.hiperion.core.service.collecting.executor;

import org.hiperion.connector.model.DataSourceResult;

import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 14.03.13.
 * Time: 14:58
 */
public interface DataSourceReadExecutor {
    Future<DataSourceResult> executeReading(ReadDataSourceTask readDataSourceTask);
}
