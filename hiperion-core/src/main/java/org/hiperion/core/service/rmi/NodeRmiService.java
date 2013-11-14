package org.hiperion.core.service.rmi;


import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.*;
import org.hiperion.connector.model.enums.NodeStatus;
import org.hiperion.connector.model.interfaces.NodeAction;
import org.hiperion.core.service.rmi.config.NodeDescriptor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.remoting.RemoteAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 27.04.13.
 * Time: 10:36
 */
public class NodeRmiService {

    private final static Logger LOGGER = Logger.getLogger(NodeRmiService.class);

    private RmiDynamicClientManager<NodeAction> rmiDynamicClientManager;

    public NodeRmiService(RmiDynamicClientManager<NodeAction> rmiDynamicClientManager) {
        this.rmiDynamicClientManager = rmiDynamicClientManager;
    }

    public void init() {

    }

    public void registerNode(NodeDescriptor nodeDescriptor) throws HiperionException {
        if (NodeStatus.UNREGISTERED == getNodeStatus(nodeDescriptor.getNodeId())) {
            final String HTTP_SCHEME = "http://";

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(HTTP_SCHEME).append(nodeDescriptor.getHostname()).append(":").
                    append(String.valueOf(nodeDescriptor.getPort())).append(nodeDescriptor.getServicePath());
            rmiDynamicClientManager.addRmiInterface(nodeDescriptor.getNodeId(), stringBuilder.toString(), NodeAction.class);
            LOGGER.info("Node registered: " + nodeDescriptor.getNodeId());
        }
    }

    public void unregisterNode(String nodeId) {
        if (NodeStatus.UNREGISTERED != getNodeStatus(nodeId)) {
            rmiDynamicClientManager.removeRmiInterface(nodeId);
        }
    }

    public NodeStatus getNodeStatus(String nodeId) {

        if (!rmiDynamicClientManager.isClientInterfaceRegistered(nodeId)) {
            return NodeStatus.UNREGISTERED;
        }

        try {
            NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(nodeId);
            nodeAction.ping();
            return NodeStatus.CONNECTED;
        } catch (RemoteAccessException e) {
            return NodeStatus.DISCONNECTED;
        } catch (ClassCastException e) {
            return NodeStatus.DISCONNECTED;
        }
    }

    public NodeAction getNodeAction(String nodeId) {
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(nodeId);
        return nodeAction;
    }

    public boolean isNodeAvailable(String nodeId) {
        if (NodeStatus.CONNECTED == getNodeStatus(nodeId)) {
            try {
                NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(nodeId);
                return nodeAction.isAvailable();
            } catch (NoSuchBeanDefinitionException e) {
                LOGGER.warn(e);
                return false;
            } catch (ClassCastException e) {
                LOGGER.warn(e);
                return false;
            }
        }
        return false;
    }

    public List<DataField> getData(String nodeId, String dataSourceServiceId, ParameterContext parameterContext)
            throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        DataRequest dataRequest = new
                DataRequest(dataSourceServiceId, parameterContext);
        DataField[] dataFields = nodeAction.getDataFields(dataRequest);
        return Arrays.asList(dataFields);
    }

    public boolean runAction(String nodeId, String actionId, ParameterContext parameterContext) throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        ActionRunRequest actionRunRequest = new
                ActionRunRequest(actionId, parameterContext);
        boolean runResult = nodeAction.runAction(actionRunRequest);
        return runResult;
    }

    public ActionDescription[] getActionDescriptions(String nodeId) throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        return nodeAction.getActionDescriptions();
    }

    public ParameterInfo[] getActionParameterInfo(String nodeId, String actionId) throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        return nodeAction.getActionParameterInfo(actionId);
    }

    public ParameterInfo[] getDataSourceServiceParameterInfo(String nodeId, String dataSourceServiceId) throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        return nodeAction.getDataSourceServiceParameterInfo(dataSourceServiceId);
    }

    public DataSourceServiceDescription[] getDataSourceServiceDescriptions(String nodeId) throws HiperionException {
        if (!isNodeAvailable(nodeId)) {
            throw new HiperionException("Node " + nodeId + " not available.");
        }
        String interfaceId = nodeId;
        NodeAction nodeAction = rmiDynamicClientManager.getClientInterfaceById(interfaceId);
        return nodeAction.getDataSourceServiceDescriptions();
    }

    public void dispose() {
        rmiDynamicClientManager.dispose();
    }

}
