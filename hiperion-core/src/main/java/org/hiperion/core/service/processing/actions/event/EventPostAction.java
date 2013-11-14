package org.hiperion.core.service.processing.actions.event;

import org.hiperion.connector.model.ParameterContext;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 22:19
 */
public class EventPostAction {

    private String nodeId;
    private String actionId;
    private ParameterContext parameterContext;

    public EventPostAction(String nodeId, String actionId, ParameterContext parameterContext) {
        this.nodeId = nodeId;
        this.actionId = actionId;
        this.parameterContext = parameterContext;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getActionId() {
        return actionId;
    }

    public ParameterContext getParameterContext() {
        return parameterContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventPostAction that = (EventPostAction) o;

        if (actionId != null ? !actionId.equals(that.actionId) : that.actionId != null) return false;
        if (nodeId != null ? !nodeId.equals(that.nodeId) : that.nodeId != null) return false;
        if (parameterContext != null ? !parameterContext.equals(that.parameterContext) : that.parameterContext != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeId != null ? nodeId.hashCode() : 0;
        result = 31 * result + (actionId != null ? actionId.hashCode() : 0);
        result = 31 * result + (parameterContext != null ? parameterContext.hashCode() : 0);
        return result;
    }
}
