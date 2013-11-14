package org.hiperion.node.service.datasource.waspmote.parser;

import org.hiperion.common.util.helpers.MapHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.util.Map;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 17:46
 */
public class WaspmoteOutput {

    private String waspmoteId;

    private Map<String,String> dataStructure;

    private long timestamp;

    public WaspmoteOutput(String waspmoteId, Map<String,String> dataStructure) {
        this.waspmoteId = waspmoteId;
        this.dataStructure = dataStructure;
        this.timestamp = new DateTime(DateTimeZone.UTC).getMillis();
    }

    public String getWaspmoteId() {
        return waspmoteId;
    }

    public Map<String, String> getDataStructure() {
        return dataStructure;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaspmoteOutput that = (WaspmoteOutput) o;

        if (timestamp != that.timestamp) return false;
        if (waspmoteId != null ? !waspmoteId.equals(that.waspmoteId) : that.waspmoteId != null) return false;
        return MapHelper.isEquals(dataStructure,that.dataStructure);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + waspmoteId != null ? waspmoteId.hashCode() : 0;
        result = 31 * result + MapHelper.getHashCode(dataStructure);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WaspmoteOutput{" +
                "waspmoteId='" + waspmoteId + '\'' +
                ", dataStructure=" + dataStructure +
                ", timestamp=" + timestamp +
                '}';
    }
}
