package org.hiperion.connector.model.enums;

import org.hiperion.common.enums.EnumString;

/**
 * User: iobestar
 * Date: 27.04.13.
 * Time: 23:32
 */
public enum ParameterType implements EnumString {
    SIMPLE("simple", "Simple named parameter with single value"),
    LIST("list", "Ordered collection of ITEM type."),
    MAP("map", "Collection of ENTRY type.");

    private final String value;

    private final String description;

    private ParameterType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
