package org.hiperion.core.service.collecting.executor;

import org.hiperion.connector.model.DataSourceResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 30.03.13.
 * Time: 12:26
 */
public class FixedThreadDataSourceReadExecutor implements DataSourceReadExecutor {

    private final static int NUMBER_OF_THREADS = 4;

    private final ExecutorService executorService;

    public FixedThreadDataSourceReadExecutor() {
        this.executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    @Override
    public Future<DataSourceResult> executeReading(ReadDataSourceTask readDataSourceTask) {
        return executorService.submit(readDataSourceTask);
    }
}
