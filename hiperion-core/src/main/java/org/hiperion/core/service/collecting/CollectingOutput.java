package org.hiperion.core.service.collecting;


import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hiperion.connector.model.DataField;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * User: iobestar
 * Date: 13.03.13.
 * Time: 14:07
 */
public class CollectingOutput {

    private String id;
    private final String streamId;
    private final long timestamp;
    private final ImmutableMap<String, DataField> dataFields;

    public CollectingOutput(String id, String streamId, long timestamp, ImmutableMap<String, DataField> dataFields) {
        this.id = id;
        this.streamId = streamId;
        this.timestamp = timestamp;
        this.dataFields = dataFields;
    }

    public CollectingOutput(String streamId, ImmutableMap<String, DataField> dataFields) {
        this.streamId = streamId;
        this.timestamp = new DateTime(DateTimeZone.UTC).getMillis();
        this.dataFields = dataFields;
    }

    public CollectingOutput(String streamId, long creationTimestamp,
                            ImmutableMap<String, DataField> dataFields) {
        this.streamId = streamId;
        this.timestamp = creationTimestamp;
        this.dataFields = dataFields;
    }

    public String getId() {
        return id;
    }

    public String getStreamId() {
        return streamId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ImmutableMap<String, DataField> getDataFields() {
        return dataFields;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CollectingOutput that = (CollectingOutput) obj;

        if (!streamId.equals(that.streamId)) return false;
        if (dataFields.size() != that.dataFields.size()) return false;

        return dataFields.equals(that.dataFields);
    }

    @Override
    public int hashCode() {
        return streamId.hashCode();
    }
}
