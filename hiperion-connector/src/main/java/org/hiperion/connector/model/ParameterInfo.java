package org.hiperion.connector.model;

import org.hiperion.connector.model.enums.ParameterType;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 27.04.13.
 * Time: 22:52
 */
public class ParameterInfo implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private String description;
    private ParameterType type;

    public ParameterInfo() {
    }

    public ParameterInfo(String name, String description, ParameterType type) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }
}
