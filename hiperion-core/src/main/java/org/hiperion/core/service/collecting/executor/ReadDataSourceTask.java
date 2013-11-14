package org.hiperion.core.service.collecting.executor;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.model.data.source.DataSource;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.03.13.
 * Time: 13:00
 */
public class ReadDataSourceTask implements Callable<DataSourceResult> {

    private final static Logger LOGGER = Logger.getLogger(ReadDataSourceTask.class);

    private DataSource dataSource;

    public ReadDataSourceTask(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSourceResult call() throws HiperionException{
        try {
            return dataSource.getDataSourceResult();
        } catch (HiperionException e) {
            LOGGER.error(e);
            throw e;
        }
    }
}
