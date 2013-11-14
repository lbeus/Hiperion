package org.hiperion.node.service;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.*;
import org.hiperion.connector.model.interfaces.NodeAction;
import org.hiperion.node.service.action.Action;
import org.hiperion.node.service.action.ActionType;
import org.hiperion.node.service.datasource.DataSourceService;
import org.hiperion.node.service.datasource.DataSourceServiceType;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 10:20
 */
@Service
public class NodeService implements NodeAction {

    private final static Logger LOGGER = Logger.getLogger(NodeService.class);

    @Autowired
    private ApplicationContext applicationContext;

    public NodeService() {
        LOGGER.info("Node service created.");
    }

    @Override
    public boolean isAvailable() {
        return true; // add logic; natively inputStream available
    }

    @Override
    public void ping() {
        return; // test method for checking inputStream node active/response to request
    }

    @Override
    public boolean runAction(ActionRunRequest actionRunRequest) throws HiperionException {
        try {

            ActionType actionType = ActionType.getActionType(actionRunRequest.getActionId());
            Action action = applicationContext.getBean(actionType.getActionClassType());
            boolean performResult = action.executeAction(actionRunRequest.getParameterContext());
            LOGGER.info("Action performed: " + actionType);
            return performResult;
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e);
            throw new HiperionException("Action " + actionRunRequest.getActionId() + " not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new HiperionException(e);
        }
    }

    @Override
    public DataField[] getDataFields(DataRequest dataRequest) throws HiperionException {
        try {
            DataSourceServiceType dataSourceServiceType =
                    DataSourceServiceType.getDataSourceServiceType(dataRequest.getDataSourceServiceId());
            DataSourceService dataSourceService = applicationContext.getBean(
                    dataSourceServiceType.getDataSourceServiceClassType());

            DataField[] dataFields = dataSourceService.getDataFields(dataRequest.getParameterContext());
            LOGGER.info("Data collected from data source: " + dataSourceServiceType);
            return dataFields;
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e);
            throw new HiperionException("Data source service " + dataRequest.getDataSourceServiceId() + " not found.");
        }
    }

    @Override
    public ActionDescription[] getActionDescriptions() {
        ActionDescription[] actionDescriptions = new
                ActionDescription[ActionType.values().length];
        int i = 0;
        for (ActionType actionType : ActionType.values()) {
            actionDescriptions[i++] = new ActionDescription(actionType.getValue(),
                    actionType.getDescription());
        }
        return actionDescriptions;
    }

    @Override
    public DataSourceServiceDescription[] getDataSourceServiceDescriptions() {

        DataSourceServiceDescription[] dataSourceServiceDescriptions = new
                DataSourceServiceDescription[DataSourceServiceType.values().length];
        int i = 0;
        for (DataSourceServiceType dataSourceServiceType : DataSourceServiceType.values()) {
            dataSourceServiceDescriptions[i++] = new DataSourceServiceDescription(dataSourceServiceType.getValue(),
                    dataSourceServiceType.getDescription());
        }
        return dataSourceServiceDescriptions;
    }

    @Override
    public ParameterInfo[] getActionParameterInfo(String actionId) throws HiperionException {
        try {

            ActionType actionType = ActionType.getActionType(actionId);
            Action action = applicationContext.getBean(actionType.getActionClassType());
            return action.getParameterInfo();
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e);
            throw new HiperionException("Action " + actionId + " not found.");
        }
    }

    @Override
    public ParameterInfo[] getDataSourceServiceParameterInfo(String dataSourceServiceId) throws HiperionException {
        try {
            DataSourceServiceType dataSourceServiceType =
                    DataSourceServiceType.getDataSourceServiceType(dataSourceServiceId);
            DataSourceService dataSourceService = applicationContext.getBean(dataSourceServiceType.getDataSourceServiceClassType());
            return dataSourceService.getParameterInfo();
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e);
            throw new HiperionException("Data source service " + dataSourceServiceId + " not found.");
        }
    }
}
