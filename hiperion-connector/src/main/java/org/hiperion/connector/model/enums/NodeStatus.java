package org.hiperion.connector.model.enums;

import org.hiperion.common.enums.EnumInt;

/**
 * User: iobestar
 * Date: 27.04.13.
 * Time: 12:57
 */
public enum NodeStatus implements EnumInt {

    UNREGISTERED(0, "Node is unregistered"),
    CONNECTED(1, "Node is connected"),
    DISCONNECTED(2, "Node is not connected.");

    private int value;

    private String description;

    private NodeStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
