package org.hiperion.node.service.datasource.waspmote;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.datasource.DataSourceService;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteOutput;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 17:31
 */
@Component
public class WaspmoteDataSourceService implements DataSourceService {

    private static final Logger LOGGER = Logger.getLogger(WaspmoteDataSourceService.class);

    private enum WaspmoteParameterDescription {
        WASPMOTE_ID("waspmote-id", "Id of waspmote device..", ParameterType.SIMPLE);

        private final String parameterName;
        private final String parameterDescription;
        private final ParameterType parameterType;

        private WaspmoteParameterDescription(String parameterName, String parameterDescription,
                                           ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    private WaspmoteParser waspmoteParser;
    private QueueItemProcessor<String> queueItemProcessor;
    private Map<String,WaspmoteOutput> waspmoteOutputMap;

    @Autowired
    public WaspmoteDataSourceService(WaspmoteParser waspmoteParser) {
        this.waspmoteParser = waspmoteParser;
        this.queueItemProcessor = new QueueItemProcessor<String>(new ItemProcessor<String>() {
            @Override
            public void process(String item) {
                processPacketItem(item);
            }
        }, new LinkedBlockingQueue<String>());
        this.waspmoteOutputMap = new ConcurrentHashMap<String, WaspmoteOutput>();
    }

    private void processPacketItem(String packetItem){
        WaspmoteOutput waspmoteOutput = waspmoteParser.parseInputPacket(packetItem);
        waspmoteOutputMap.put(waspmoteOutput.getWaspmoteId(),waspmoteOutput);
    }

    public void submitForProcessing(String dataStructure){
        queueItemProcessor.queue(dataStructure);
    }

    @PostConstruct
    @Override
    public void init() {
        queueItemProcessor.start();
        LOGGER.info("Waspmote data service started.");
    }

    private WaspmoteDataSourceServiceParameter getWaspmoteDataSourceServiceParameter(ParameterContext parameterContext){
        final String waspmoteId = parameterContext.getSimpleParameters().get(
                WaspmoteParameterDescription.WASPMOTE_ID.parameterName);
        return new WaspmoteDataSourceServiceParameter() {
            @Override
            public String getWaspmoteId() {
                return waspmoteId;
            }
        };
    }

    @Override
    public DataField[] getDataFields(ParameterContext parameterContext) throws HiperionException {
        WaspmoteDataSourceServiceParameter waspmoteDataSourceServiceParameter =
                getWaspmoteDataSourceServiceParameter(parameterContext);

        WaspmoteOutput waspmoteOutput = waspmoteOutputMap.get(waspmoteDataSourceServiceParameter.getWaspmoteId());
        if (null == waspmoteOutput){
            return new DataField[0];
        }

        int numberOfDataFields = waspmoteOutput.getDataStructure().values().size();
        DataField[] dataFields = new DataField[numberOfDataFields];
        int i = 0;
        for(Map.Entry<String,String> entry : waspmoteOutput.getDataStructure().entrySet()){
            DataField dataField = new DataField(entry.getKey(), DataType.STRING,entry.getValue(),waspmoteOutput.getTimestamp());
            dataFields[i++] = dataField;
        }

        return dataFields;
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = WaspmoteParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (WaspmoteParameterDescription waspmoteParameterDescription : WaspmoteParameterDescription.values()) {
            result[i++] = new ParameterInfo(waspmoteParameterDescription.parameterName,
                    waspmoteParameterDescription.parameterDescription,
                    waspmoteParameterDescription.parameterType);
        }
        return result;
    }

    @PreDestroy
    @Override
    public void dispose() {
        queueItemProcessor.stop();
        LOGGER.info("Waspmote data service stopped.");
    }
}
