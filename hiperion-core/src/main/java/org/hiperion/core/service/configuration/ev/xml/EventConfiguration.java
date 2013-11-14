package org.hiperion.core.service.configuration.ev.xml;

import org.apache.log4j.Logger;
import org.hiperion.core.service.configuration.HiperionConfiguration;
import org.hiperion.core.service.processing.actions.event.Event;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 11:54
 */
public interface EventConfiguration extends HiperionConfiguration<String, String, Event> {
}
