package org.hiperion.core.web.controller.service.configuration;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 19.05.13.
 * Time: 20:04
 */
public class ProcessingRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String actionId;
    private String streamId;

    public ProcessingRestBean() {
    }

    public ProcessingRestBean(String actionId, String streamId) {
        this.actionId = actionId;
        this.streamId = streamId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
