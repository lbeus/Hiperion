package org.hiperion.core.service.processing.actions.store;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 19.04.13.
 * Time: 10:03
 */
public interface StoreService {
    void store(CollectingOutput collectingOutput) throws HiperionException;
}
