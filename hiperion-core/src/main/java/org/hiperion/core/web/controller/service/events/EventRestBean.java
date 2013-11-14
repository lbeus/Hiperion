package org.hiperion.core.web.controller.service.events;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 18.05.13.
 * Time: 13:48
 */
public class EventRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private boolean publishable;
    private boolean stateful;
    private String streamId;
    private String description;
    private boolean registered;

    public EventRestBean() {
    }

    public EventRestBean(String name, boolean publishable, boolean stateful, String streamId, String description,
                         boolean registered) {
        this.name = name;
        this.publishable = publishable;
        this.stateful = stateful;
        this.streamId = streamId;
        this.description = description;
        this.registered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublishable() {
        return publishable;
    }

    public void setPublishable(boolean publishable) {
        this.publishable = publishable;
    }

    public boolean isStateful() {
        return stateful;
    }

    public void setStateful(boolean stateful) {
        this.stateful = stateful;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
