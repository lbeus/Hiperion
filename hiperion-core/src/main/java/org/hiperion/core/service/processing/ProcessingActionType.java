package org.hiperion.core.service.processing;

import org.hiperion.common.enums.EnumString;
import org.hiperion.core.service.processing.actions.archive.ArchiveProcessingAction;
import org.hiperion.core.service.processing.actions.event.EventProcessingAction;
import org.hiperion.core.service.processing.actions.publish.PublishOutputProcessingAction;
import org.hiperion.core.service.processing.actions.stdout.StdOutProcessingAction;
import org.hiperion.core.service.processing.actions.store.StoreProcessingAction;

/**
 * User: iobestar
 * Date: 25.03.13.
 * Time: 20:33
 */
public enum ProcessingActionType implements EnumString {

    EVENT_GENERATOR("event-generator", "Generates event based on configuration and performs post action."){
        @Override
        public Class<? extends ProcessingAction> getProcessingActionType() {
            return EventProcessingAction.class;
        }
    },
    STORE("store", "Stores collecting output to local database. "){
        @Override
        public Class<? extends ProcessingAction> getProcessingActionType() {
            return StoreProcessingAction.class;
        }
    },
//    ARCHIVE("archive", "Archives collecting output to archive database."){
//        @Override
//        public Class<? extends ProcessingAction> getProcessingActionType() {
//            return ArchiveProcessingAction.class;
//        }
//    },
    PUBLISH("publish","Live data publishing."){
        @Override
        public Class<? extends ProcessingAction> getProcessingActionType() {
            return PublishOutputProcessingAction.class;
        }
    },
    STD_OUTPUT("std-output", "Writes collected data to standard output."){
        @Override
        public Class<? extends ProcessingAction> getProcessingActionType() {
            return StdOutProcessingAction.class;
        }
    };

    private final String value;
    private final String description;

    private ProcessingActionType(String value, String description) {
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

    public abstract Class<? extends ProcessingAction> getProcessingActionType();

    public static ProcessingActionType getProcessingActionType(String value) {
        for (ProcessingActionType processingActionType : ProcessingActionType.values()) {
            if (processingActionType.getValue().equals(value)) {
                return processingActionType;
            }
        }
        throw new IllegalArgumentException();
    }
}
