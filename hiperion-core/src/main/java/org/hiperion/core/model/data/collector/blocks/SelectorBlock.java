package org.hiperion.core.model.data.collector.blocks;

import com.google.common.collect.ImmutableMap;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.core.model.data.collector.DataCollectorCache;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.util.DataKeyCreator;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.03.13.
 * Time: 19:24
 */
public class SelectorBlock implements CollectingBlock {

    private String selectorName;
    private final Collection<SelectStatement> selectStatements;

    public SelectorBlock(String selectorName,
                         Collection<SelectStatement> selectStatements) {
        this.selectorName = selectorName;
        this.selectStatements = selectStatements;
    }

    public String getSelectorName() {
        return selectorName;
    }

    @Override
    public void executeBlock(String collectorId, DataCollectorCache dataCollectorCache) throws HiperionException {

        String streamId = DataKeyCreator.createKey(collectorId, selectorName);
        HashMap<String, DataField> dataFields = new HashMap<String, DataField>();
        for (SelectStatement selectStatement : selectStatements) {
            // Select from cache
            DataField originalDataField = dataCollectorCache.getDataField(
                    selectStatement.getDataSourceName(),
                    selectStatement.getDataFieldName());
            if (selectStatement.getOutputDataFieldName() != null) {
                // add to output with custom name
                dataFields.put(selectStatement.getOutputDataFieldName(), new DataField(selectStatement.getOutputDataFieldName(), originalDataField.getDataType(),
                        originalDataField.getValue(), originalDataField.getTimestamp()));
                continue;
            }
            dataFields.put(originalDataField.getName(), originalDataField);
        }

        ImmutableMap<String, DataField> immutableDataFieldMap = ImmutableMap.copyOf(dataFields);
        CollectingOutput collectingOutput = new CollectingOutput(
                streamId, immutableDataFieldMap);
        dataCollectorCache.addToCache(collectingOutput);
    }
}
