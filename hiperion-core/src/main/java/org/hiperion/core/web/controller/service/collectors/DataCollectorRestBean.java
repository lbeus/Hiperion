package org.hiperion.core.web.controller.service.collectors;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 12.05.13.
 * Time: 16:59
 */
public class DataCollectorRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private String description;
    private String type;
    private String cronExpression;
    private boolean registered;

    public DataCollectorRestBean() {
    }

    public DataCollectorRestBean(String name, String description, String type,
                                 String cronExpression, boolean registered) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cronExpression = cronExpression;
        this.registered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public boolean getRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
