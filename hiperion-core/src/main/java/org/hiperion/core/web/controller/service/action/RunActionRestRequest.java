package org.hiperion.core.web.controller.service.action;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 22.05.13.
 * Time: 19:16
 */
public class RunActionRestRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    private String nodeId;
    private String actionId;
    private String parametersXml;

    public RunActionRestRequest() {
    }

    public RunActionRestRequest(String nodeId, String actionId, String parametersXml) {
        this.nodeId = nodeId;
        this.actionId = actionId;
        this.parametersXml = parametersXml;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getParametersXml() {
        return parametersXml;
    }

    public void setParametersXml(String parametersXml) {
        this.parametersXml = parametersXml;
    }
}
