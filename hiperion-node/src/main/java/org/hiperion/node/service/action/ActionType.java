package org.hiperion.node.service.action;

import org.hiperion.common.enums.EnumString;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.node.service.action.email.EmailNotificationAction;
import org.hiperion.node.service.action.stdoutput.StdOutputAction;
import org.hiperion.node.service.action.read.datasource.ReadDataSourceAction;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 10:33
 */
public enum ActionType implements EnumString {
    STD_OUTPUT("std-output", "Write text parameter to standard output.") {
        @Override
        public Class<? extends Action> getActionClassType() {
            return StdOutputAction.class;
        }
    },
    READ_DATA_SOURCE("read-data-source", "Read data trough arbitrary data source service."){
        @Override
        public Class<? extends Action> getActionClassType() {
            return ReadDataSourceAction.class;
        }
    },
    EMAIL_NOT("email-not", "Send email notifications"){
        @Override
        public Class<? extends Action> getActionClassType() {
            return EmailNotificationAction.class;
        }
    };

    private final String value;
    private final String description;

    private ActionType(String value, String description) {
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

    public abstract Class<? extends Action> getActionClassType();

    public static ActionType getActionType(String value) throws HiperionException {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getValue().equals(value)) {
                return actionType;
            }
        }
        throw new HiperionException("Action with name " + value + " not found.");
    }
}
