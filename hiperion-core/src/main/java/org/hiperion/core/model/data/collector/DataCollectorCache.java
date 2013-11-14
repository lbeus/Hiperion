package org.hiperion.core.model.data.collector;

import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.core.service.collecting.CollectingOutput;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.03.13.
 * Time: 10:09
 */
public class DataCollectorCache {

    private final Map<String, DataSourceResult> acquisitionCache;
    private final Collection<CollectingOutput> collectingCache;

    public static DataCollectorCache newInstance() {
        return new DataCollectorCache();
    }

    private DataCollectorCache() {
        this.acquisitionCache = new HashMap<String, DataSourceResult>();
        this.collectingCache = new LinkedList<CollectingOutput>();
    }

    public void addToCache(String dataSourceName,
                           DataSourceResult dataSourceResult) {
        acquisitionCache.put(dataSourceName, dataSourceResult);
    }

    public void addToCache(CollectingOutput collectingOutput) {
        collectingCache.add(collectingOutput);
    }

    public ArrayList<CollectingOutput> getCollectingOutputs() {
        ArrayList<CollectingOutput> collectingOutputs = new ArrayList<CollectingOutput>(collectingCache);
        collectingCache.clear();
        return collectingOutputs;
    }

    public DataField getDataField(String dataSourceName, String fieldName) {

        DataSourceResult cacheDataSourceResult = acquisitionCache.get(dataSourceName);
        if (null == cacheDataSourceResult) {
            return new DataField(fieldName, DataType.UNDEFINED, "ERROR");
        }

        DataField cacheDataField = cacheDataSourceResult.getDataField(fieldName);
        if (null == cacheDataField) {
            return new DataField(fieldName, DataType.UNDEFINED, "ERROR");
        }
        return acquisitionCache.get(dataSourceName).getDataField(fieldName);
    }
}
