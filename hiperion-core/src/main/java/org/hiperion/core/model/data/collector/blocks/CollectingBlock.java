package org.hiperion.core.model.data.collector.blocks;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.DataCollectorCache;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 12.03.13.
 * Time: 16:40
 */
public interface CollectingBlock {
    void executeBlock(String collectorId, DataCollectorCache dataCollectorCache) throws HiperionException;
}
