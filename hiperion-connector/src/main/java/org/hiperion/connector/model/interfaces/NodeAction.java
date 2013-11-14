package org.hiperion.connector.model.interfaces;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.*;

import java.util.List;

/**
 * User: iobestar
 * Date: 09.04.13.
 * Time: 20:46
 */
public interface NodeAction {

    boolean runAction(ActionRunRequest actionRunRequest) throws HiperionException;

    DataField[] getDataFields(DataRequest dataRequest) throws HiperionException;

    boolean isAvailable();

    void ping();

    ActionDescription[] getActionDescriptions() throws HiperionException;

    DataSourceServiceDescription[] getDataSourceServiceDescriptions() throws HiperionException;

    ParameterInfo[] getActionParameterInfo(String actionId) throws HiperionException;

    ParameterInfo[] getDataSourceServiceParameterInfo(String dataSourceServiceId) throws HiperionException;
}
