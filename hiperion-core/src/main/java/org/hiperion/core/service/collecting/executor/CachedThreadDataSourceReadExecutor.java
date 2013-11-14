package org.hiperion.core.service.collecting.executor;

import org.hiperion.connector.model.DataSourceResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 30.03.13.
 * Time: 12:33
 */
public class CachedThreadDataSourceReadExecutor implements DataSourceReadExecutor {

    private final ExecutorService executorService;

    public CachedThreadDataSourceReadExecutor() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public Future<DataSourceResult> executeReading(ReadDataSourceTask readDataSourceTask) {
        return executorService.submit(readDataSourceTask);
    }
}
