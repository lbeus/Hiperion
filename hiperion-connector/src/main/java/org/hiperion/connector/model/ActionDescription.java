package org.hiperion.connector.model;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 29.04.13.
 * Time: 20:36
 */
public class ActionDescription implements Serializable {

    private final static long serialVersionUID = 1L;

    private String actionId;
    private String actionDescription;

    public ActionDescription() {
    }

    public ActionDescription(String actionId, String actiondescription) {
        this.actionId = actionId;
        this.actionDescription = actiondescription;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
}
