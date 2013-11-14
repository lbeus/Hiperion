package org.hiperion.node.service.datasource;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;

import java.util.List;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 14:58
 */
public interface DataSourceService {
    void init();

    DataField[] getDataFields(ParameterContext parameterContext) throws HiperionException;

    ParameterInfo[] getParameterInfo();

    void dispose();
}
