package org.hiperion.core.service.processing.actions.event.publisher;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 11:59
 */
public class PublishEvent {
    private String eventId;
    private String description;
    private boolean eventState;
    private long timestamp;

    public PublishEvent() {
    }

    public PublishEvent(String eventId, String description, boolean eventState, long timestamp) {
        this.eventId = eventId;
        this.description = description;
        this.eventState = eventState;
        this.timestamp = timestamp;
    }

    public PublishEvent(String eventId, String description, boolean eventState) {
        this.eventId = eventId;
        this.description = description;
        this.eventState = eventState;
        this.timestamp = new DateTime(DateTimeZone.UTC).getMillis();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEventState() {
        return eventState;
    }

    public void setEventState(boolean eventState) {
        this.eventState = eventState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublishEvent publishEvent = (PublishEvent) o;

        if (timestamp != publishEvent.timestamp) return false;
        if (description != null ? !description.equals(publishEvent.description) : publishEvent.description != null)
            return false;
        if (eventId != null ? !eventId.equals(publishEvent.eventId) : publishEvent.eventId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PublishEvent{" +
                "eventId='" + eventId + '\'' +
                ", description='" + description + '\'' +
                ", eventState=" + eventState +
                ", timestamp=" + timestamp +
                '}';
    }
}
