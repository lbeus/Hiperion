package org.hiperion.core.web.controller.service.configuration;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 19.05.13.
 * Time: 20:11
 */
public class ActionNameRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String actionId;
    private String actionDescription;

    public ActionNameRestBean() {
    }

    public ActionNameRestBean(String actionId, String actionDescription) {
        this.actionId = actionId;
        this.actionDescription = actionDescription;
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
