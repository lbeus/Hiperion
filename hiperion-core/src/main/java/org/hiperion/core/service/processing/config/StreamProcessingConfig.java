package org.hiperion.core.service.processing.config;

import org.hiperion.core.service.processing.ProcessingActionType;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 10.04.13.
 * Time: 20:45
 */
public interface StreamProcessingConfig {
    boolean isProcessingEnabled(String streamId, ProcessingActionType processingActionType);

    void enableProcessingFor(String streamId, ProcessingActionType processingActionType);

    void disableProcessingFor(String streamId, ProcessingActionType processingActionType);
}
