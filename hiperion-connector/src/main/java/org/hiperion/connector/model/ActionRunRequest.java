package org.hiperion.connector.model;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 13.04.13.
 * Time: 22:42
 */
public class ActionRunRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    private String actionId;
    private ParameterContext parameterContext;

    public ActionRunRequest() {
    }

    public ActionRunRequest(String actionId, ParameterContext parameterContext) {
        this.actionId = actionId;
        this.parameterContext = parameterContext;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public ParameterContext getParameterContext() {
        return parameterContext;
    }

    public void setParameterContext(ParameterContext parameterContext) {
        this.parameterContext = parameterContext;
    }
}
