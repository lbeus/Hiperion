package org.hiperion.core.service.processing.actions.event;

import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;

import java.util.List;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 18:22
 */
public class Event {

    private String eventId;
    private boolean stateful;
    private boolean publishable;
    private String streamId;
    private String description;
    private EventCondition eventCondition;
    private List<EventPostAction> eventPostActions;

    private boolean eventState;

    public Event(String eventId, String description, boolean stateful, boolean publishable, String streamId,
                 EventCondition eventCondition, List<EventPostAction> eventPostActions) {
        this.eventId = eventId;
        this.description = description;
        this.stateful = stateful;
        this.publishable = publishable;
        this.streamId = streamId;
        this.eventCondition = eventCondition;
        this.eventPostActions = eventPostActions;
        this.eventState = false;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isStateful() {
        return stateful;
    }

    public boolean isPublishable() {
        return publishable;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getDescription() {
        return description;
    }

    public EventCondition getEventCondition() {
        return eventCondition;
    }

    public List<EventPostAction> getEventPostActions() {
        return eventPostActions;
    }

    public boolean getEventState() {
        return eventState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (eventState != event.eventState) return false;
        if (publishable != event.publishable) return false;
        if (stateful != event.stateful) return false;
        if (eventCondition != null ? !eventCondition.equals(event.eventCondition) : event.eventCondition != null)
            return false;
        if (description != null ? !description.equals(event.description) : event.description != null)
            return false;
        if (eventId != null ? !eventId.equals(event.eventId) : event.eventId != null) return false;
        if (eventPostActions != null ? !eventPostActions.equals(event.eventPostActions) : event.eventPostActions != null)
            return false;
        if (streamId != null ? !streamId.equals(event.streamId) : event.streamId != null) return false;

        return true;
    }

    public boolean checkCondition(CollectingOutput collectingOutput) {
        boolean checkConditionResult = eventCondition.checkCondition(collectingOutput);

        if (!stateful) {
            return checkConditionResult;
        }

        if (eventState != checkConditionResult) {
            eventState = checkConditionResult;
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (stateful ? 1 : 0);
        result = 31 * result + (publishable ? 1 : 0);
        result = 31 * result + (streamId != null ? streamId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (eventCondition != null ? eventCondition.hashCode() : 0);
        result = 31 * result + (eventPostActions != null ? eventPostActions.hashCode() : 0);
        result = 31 * result + (eventState ? 1 : 0);
        return result;
    }
}
