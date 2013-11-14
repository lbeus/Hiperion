package org.hiperion.core.service.configuration.ev.xml;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.*;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.model.data.collector.DataCollectorType;
import org.hiperion.core.service.configuration.dc.xml.XmlDataCollectorConfigurationParser;
import org.hiperion.core.service.processing.actions.event.Event;
import org.hiperion.core.service.processing.actions.event.EventPostAction;
import org.hiperion.core.service.processing.actions.event.EventProcessingAction;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 06.05.13.
 * Time: 20:09
 */
public class XmlEventConfiguration implements EventConfiguration {

    private static final Logger LOGGER = Logger.getLogger(EventConfiguration.class);

    private final ConcurrentHashMap<String, Event> events;
    private final File storeDirectory;

    public XmlEventConfiguration(File storeDirectory) {
        this.events = new ConcurrentHashMap<String, Event>();
        this.storeDirectory = storeDirectory;
    }

    @Override
    public void init() {
        if (!storeDirectory.isDirectory()) {
            return;
        }
        for (File xmlFile : storeDirectory.listFiles()) {
            if (xmlFile.isFile()) {
                XmlEventConfigurationParser xmlEventConfigurationParser = new
                        XmlEventConfigurationParser(xmlFile);
                String eventId = xmlEventConfigurationParser.getEventId();
                events.putIfAbsent(eventId,createEvent(xmlEventConfigurationParser));
            }
        }
    }

    @Override
    public void addConfiguration(String eventXml) throws HiperionException {
        try {
            XmlEventConfigurationParser xmlEventConfigurationParser = new
                    XmlEventConfigurationParser(eventXml);
            String eventId = xmlEventConfigurationParser.getEventId();

            Event event = events.get(eventId);
            if (null != event) {
                removeConfiguration(eventId);
            }

            FileUtils.writeStringToFile(new File(storeDirectory, eventId + ".xml"), eventXml);
            reloadConfiguration(eventId);
        } catch (IOException e) {
            LOGGER.error(e);
        } catch (EventNotFound e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void removeConfiguration(String configurationId) throws HiperionException{
        Event event = events.get(configurationId);
        if (null == event) {
            return;
        }

        File eventXmlFile = new File(storeDirectory, configurationId + ".xml");
        if (eventXmlFile.exists() && eventXmlFile.isFile()) {
            eventXmlFile.delete();
        }
        events.remove(configurationId);
    }

    @Override
    public void reloadConfiguration(String configurationId) throws HiperionException {
        File eventXmlFile = new File(storeDirectory, configurationId + ".xml");
        if (!eventXmlFile.exists() || !eventXmlFile.isFile()) {
            throw new EventNotFound();
        }

        XmlEventConfigurationParser xmlEventConfigurationParser = new
                XmlEventConfigurationParser(eventXmlFile);

        String eventId = xmlEventConfigurationParser.getEventId();
        events.put(eventId, createEvent(xmlEventConfigurationParser));
    }

    @Override
    public Event findById(String configurationId) throws EventNotFound {
        Event event = events.get(configurationId);
        if (null == event) {
            throw new EventNotFound();
        }
        return event;
    }

    @Override
    public Collection<Event> findAll() {
        return events.values();
    }

    private Event createEvent(XmlEventConfigurationParser xmlEventConfigurationParser){
        String eventId = xmlEventConfigurationParser.getEventId();
        boolean stateful = xmlEventConfigurationParser.getStateful();
        boolean publishable = xmlEventConfigurationParser.getPublishable();
        String description = xmlEventConfigurationParser.getDescription();
        String streamId = xmlEventConfigurationParser.getStreamId();
        EventCondition eventCondition = xmlEventConfigurationParser.getRiseCondition();
        List<EventPostAction> postActions = xmlEventConfigurationParser.getEventPostActions();

        return new Event(eventId,description,stateful,publishable,streamId,eventCondition,postActions);

    }

    @Override
    public String xmlConfigurationContent(String configurationId) throws HiperionException {
        Event event = events.get(configurationId);
        if(null == event){
            throw new EventNotFound();
        }

        File eventXmlFile = new File(storeDirectory, configurationId + ".xml");
        try {
            return FileUtils.readFileToString(eventXmlFile);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
