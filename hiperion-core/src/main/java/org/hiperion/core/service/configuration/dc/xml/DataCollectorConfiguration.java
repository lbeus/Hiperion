package org.hiperion.core.service.configuration.dc.xml;

import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.service.configuration.HiperionConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 05.05.13.
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public interface DataCollectorConfiguration extends HiperionConfiguration<String, String, DataCollector> {
}
