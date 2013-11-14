package org.hiperion.node.service.action.read.datasource;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.exception.ParameterConversionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.PushDataRequest;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.action.Action;
import org.hiperion.node.service.datasource.DataSourceService;
import org.hiperion.node.service.datasource.DataSourceServiceType;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 13:46
 */
@Component
public class ReadDataSourceAction implements Action {

    private final static Logger LOGGER = Logger.getLogger(ReadDataSourceAction.class);

    @Autowired
    private ApplicationContext applicationContext;

    private final PushDataService pushDataService;

    private enum ReadDataSourceParameterDescription {
        PUSH_COLLECTOR_ID("push-collector-id", "Id of push collector on Hiperion Core instance.", ParameterType.SIMPLE),
        DATA_SOURCE_ID("data-source-id", "Id of data source on push collector instance.", ParameterType.SIMPLE),
        PUSH_URI("push-uri", "URI of push service method.", ParameterType.SIMPLE),
        DATA_SOURCE_SERVICE_ID("data-source-service-id", "Id of data source service on Hiperion Node instance.", ParameterType.SIMPLE),
        DESCRIPTION_FIELD_NAME("description-field-name", "Description value.", ParameterType.SIMPLE),
        DESCRIPTION_FIELD_VALUE("description-field-value", "Description value.", ParameterType.SIMPLE);

        private String parameterName;
        private String parameterDescription;
        private ParameterType parameterType;

        private ReadDataSourceParameterDescription(String parameterName, String parameterDescription,
                                                      ParameterType parameterType){
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    @Autowired
    public ReadDataSourceAction(PushDataService pushDataService) {
        this.pushDataService = pushDataService;
    }

    @PostConstruct
    @Override
    public void init() {
        LOGGER.info("Action initialized");
    }

    private ReadDataSourceActionParameter getReadDataSourceActionParameter(final ParameterContext parameterContext)
            throws ParameterConversionException {

        final String pushCollectorId = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.PUSH_COLLECTOR_ID.parameterName);

        final String dataSourceId = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.DATA_SOURCE_ID.parameterName);

        final String pushUri = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.PUSH_URI.parameterName);

        final String dataSourceServiceId = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.DATA_SOURCE_SERVICE_ID.parameterName);

        final String descriptionFieldName = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.DESCRIPTION_FIELD_NAME.parameterName);

        final String descriptionFieldValue = parameterContext.getSimpleParameters().get(
                ReadDataSourceParameterDescription.DESCRIPTION_FIELD_VALUE.parameterName);

        ReadDataSourceActionParameter readDataSourceActionParameter = new
                ReadDataSourceActionParameter() {
                    @Override
                    public String getPushCollectorId() {
                        return pushCollectorId;
                    }

                    @Override
                    public String getDataSourceId() {
                        return dataSourceId;
                    }

                    @Override
                    public String getPushUri() {
                        return pushUri;
                    }

                    @Override
                    public String getDataSourceServiceId() {
                        return dataSourceServiceId;
                    }

                    @Override
                    public String getDescriptionFieldName() {
                        return descriptionFieldName;
                    }

                    @Override
                    public String getDescriptionFieldValue() {
                        return descriptionFieldValue;
                    }
                };
        return readDataSourceActionParameter;
    }

    @Override
    public boolean executeAction(ParameterContext parameterContext) throws HiperionException {
        ReadDataSourceActionParameter readDataSourceActionParameter =
                getReadDataSourceActionParameter(parameterContext);

        try {
            DataSourceServiceType dataSourceServiceType =
                    DataSourceServiceType.getDataSourceServiceType(
                            readDataSourceActionParameter.getDataSourceServiceId());
            DataSourceService dataSourceService = applicationContext.getBean(
                    dataSourceServiceType.getDataSourceServiceClassType());

            DataField[] dataFields = dataSourceService.getDataFields(parameterContext);

            Map<String,DataField> dataFieldsMap = new HashMap<String, DataField>();
            for(DataField dataField : dataFields){
                dataFieldsMap.put(dataField.getName(),dataField);
            }
            DataField descriptionField = new DataField(readDataSourceActionParameter.getDescriptionFieldName(),
                    DataType.STRING, readDataSourceActionParameter.getDescriptionFieldValue());
            dataFieldsMap.put(descriptionField.getName(),descriptionField);

            PushDataRequest pushDataRequest = new PushDataRequest();
            pushDataRequest.setDataCollectorId(readDataSourceActionParameter.getPushCollectorId());
            pushDataRequest.setDataSourceId(readDataSourceActionParameter.getDataSourceId());
            pushDataRequest.setDataFields(dataFieldsMap);

            PushDataRequest[] pushDataRequests = new PushDataRequest[1];
            pushDataRequests[0] = pushDataRequest;
            pushDataService.pushData(readDataSourceActionParameter.getPushUri(), pushDataRequests);
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e);
            throw new HiperionException("Data source service " +
                    readDataSourceActionParameter.getDataSourceServiceId() + " not found.");
        } catch (Exception e){
            LOGGER.error(e);
            throw new HiperionException(e);
        }
        return true;
    }

    @Override
    public void dispose() {
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = ReadDataSourceParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (ReadDataSourceParameterDescription readDataSourceParameterDescription : ReadDataSourceParameterDescription.values()) {
            result[i++] = new ParameterInfo(readDataSourceParameterDescription.parameterName,
                    readDataSourceParameterDescription.parameterDescription,
                    readDataSourceParameterDescription.parameterType);
        }
        return result;
    }
}
