package org.hiperion.core.web.controller.service.events;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.EventNotFound;
import org.hiperion.common.exception.EventRegistered;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.configuration.ev.xml.EventConfiguration;
import org.hiperion.core.service.configuration.ev.xml.XmlEventConfigurationParser;
import org.hiperion.core.service.processing.actions.event.Event;
import org.hiperion.core.service.processing.actions.event.EventProcessingAction;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.hiperion.core.web.controller.service.XmlRestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 18.05.13.
 * Time: 13:47
 */
@Controller
@RequestMapping("/service/events")
public class EventsController {

    private static final Logger LOGGER = Logger.getLogger(EventsController.class);

    @Autowired
    private EventProcessingAction eventProcessingAction;

    @Autowired
    private EventConfiguration eventConfiguration;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EventRestBean> getEvents() {
        List<EventRestBean> eventBeans = new LinkedList<EventRestBean>();
        try {
            for (Event event : eventConfiguration.findAll()) {
                String eventId = event.getEventId();
                boolean stateful = event.isStateful();
                boolean publishable = event.isPublishable();
                String streamId = event.getStreamId();
                String description = event.getDescription();
                boolean registered = eventProcessingAction.isRegistered(eventId);
                eventBeans.add(new EventRestBean(eventId, publishable, stateful, streamId, description, registered));
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return eventBeans;
    }

    @RequestMapping(headers = {"content-type=application/json"}, value = "/event/add", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse addEvent(@RequestBody XmlRestBean eventRequest) {
        XmlEventConfigurationParser xmlEventConfigurationParser =
                new XmlEventConfigurationParser(eventRequest.getXmlContent());
        try {
            String eventId = xmlEventConfigurationParser.getEventId();
            String streamId = xmlEventConfigurationParser.getStreamId();
            if(eventProcessingAction.isRegistered(eventId)){
                return new HiperionRestResponse(HiperionRestResponse.ADD_EVENT_REGISTERED);
            }
            eventConfiguration.addConfiguration(eventRequest.getXmlContent());
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (EventNotFound e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.EVENT_NOT_FOUND);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/event/{eventId}/reload", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse reloadEvent(@PathVariable String eventId) {
        try {
            if(eventProcessingAction.isRegistered(eventId)){
                return new HiperionRestResponse(HiperionRestResponse.RELOAD_EVENT_REGISTERED);
            }
            eventConfiguration.reloadConfiguration(eventId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (EventNotFound e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.EVENT_NOT_FOUND);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/event/{eventId}/remove", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse removeEvent(@PathVariable String eventId) {
        try {
            if(eventProcessingAction.isRegistered(eventId)){
                return new HiperionRestResponse(HiperionRestResponse.REMOVE_EVENT_REGISTERED);
            }
            eventConfiguration.removeConfiguration(eventId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (EventNotFound e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.EVENT_NOT_FOUND);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/event/{eventId}/register", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse registerEvent(@PathVariable String eventId) {
        try {
            Event event = eventConfiguration.findById(eventId);
            if (eventProcessingAction.isRegistered(eventId)) {
                return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
            }
            eventProcessingAction.registerEvent(event);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/event/{eventId}/unregister", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse unregisterEvent(@PathVariable String eventId) {
        try {
            Event event = eventConfiguration.findById(eventId);
            if (!eventProcessingAction.isRegistered(eventId)) {
                return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
            }
            eventProcessingAction.unregisterEvent(event);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/event/{eventId}/xml", method = RequestMethod.GET)
    public
    @ResponseBody
    XmlRestBean getXmlContent(@PathVariable String eventId) {
        try {
            String xmlContent = eventConfiguration.xmlConfigurationContent(eventId);
            return new XmlRestBean(xmlContent);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return null;
        }
    }
}
