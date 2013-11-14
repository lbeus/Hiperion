package org.hiperion.core.service.processing.actions.event;

import com.google.common.collect.ImmutableSet;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.ProcessingAction;
import org.hiperion.core.service.processing.actions.event.publisher.EventPublisher;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;
import org.hiperion.core.service.rmi.NodeRmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 18:20
 */
@Component
public class EventProcessingAction implements ProcessingAction {

    private final static Logger LOGGER = Logger.getLogger(EventProcessingAction.class);

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Event>> streamEventMap;

    private final NodeRmiService nodeRmiService;
    private final EventPublisher eventPublisher;

    @Autowired
    public EventProcessingAction(NodeRmiService nodeRmiService, EventPublisher eventPublisher) {
        this.streamEventMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, Event>>();
        this.nodeRmiService = nodeRmiService;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public void processCollectingOutput(CollectingOutput collectingOutput) {
        String streamId = collectingOutput.getStreamId();
        LOGGER.info("Checking events for stream " + streamId);
        ConcurrentHashMap<String, Event> streamEvents = streamEventMap.get(streamId);
        if (null == streamEvents) {
            return;
        }
        for (Event event : streamEvents.values()) {
            if (!event.checkCondition(collectingOutput)) {
                continue;
            }

            if (event.isPublishable()) {
                PublishEvent publishEvent = new PublishEvent(event.getEventId(),
                        event.getDescription(), event.getEventState());
                eventPublisher.publishEvent(publishEvent);
            }

            for (EventPostAction eventPostAction : event.getEventPostActions()) {
                try {
                    nodeRmiService.runAction(eventPostAction.getNodeId(),
                            eventPostAction.getActionId(), eventPostAction.getParameterContext());
                } catch (HiperionException e) {
                    LOGGER.error(e);
                }
            }
        }
    }

    public void registerEvent(Event event) {
        String streamId = event.getStreamId();
        ConcurrentHashMap<String, Event> streamEvents = streamEventMap.get(streamId);
        if (null == streamEvents) {
            streamEvents = new ConcurrentHashMap<String, Event>();
            streamEventMap.put(streamId, streamEvents);
        }
        streamEvents.putIfAbsent(event.getEventId(), event);
        LOGGER.info("Event " + event.getEventId() + " registered.");
    }

    public void unregisterEvent(Event event) {
        String streamId = event.getStreamId();
        ConcurrentHashMap<String, Event> streamEvents = streamEventMap.get(streamId);
        if (null == streamEvents) {
            return;
        }
        streamEvents.remove(event.getEventId());
        LOGGER.info("Event " + event.getEventId() + " unregistered.");
    }

    public ImmutableSet<String> getRegisteredEvents(){
        Set<String> registeredEvents = new HashSet<String>();
        for(ConcurrentHashMap<String,Event> eventsMap : streamEventMap.values() ){
            registeredEvents.addAll(eventsMap.keySet());
        }
        return ImmutableSet.copyOf(registeredEvents);
    }

    public boolean isRegistered(String eventId){
        Set<String> registeredEvents = getRegisteredEvents();
        return registeredEvents.contains(eventId);
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
