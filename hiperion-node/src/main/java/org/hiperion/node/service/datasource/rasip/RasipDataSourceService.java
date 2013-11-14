package org.hiperion.node.service.datasource.rasip;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.datasource.DataSourceService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 17:20
 */
@Component
public class RasipDataSourceService implements DataSourceService {

    private final static Logger LOGGER = Logger.getLogger(RasipDataSourceService.class);

    private final static Charset DEFAULT_CHARSET = Charset.forName(System.getProperty("hiperion.node.default.charset"));

    private enum RasipParameterDescription {
        TEMP_URI("rasip-live-temp-uri", "Rasip temperature URI.", ParameterType.SIMPLE),
        LIGHT_TEMP_URI("rasip-live-light-temp-uri", "Rasip light temperature URI.", ParameterType.SIMPLE),
        LIGHT_LIGHT_URI("rasip-live-light-light-uri", "Rasip light light URI.", ParameterType.SIMPLE);

        private final String parameterName;

        private final String parameterDescription;

        private final ParameterType parameterType;

        private RasipParameterDescription(String parameterName, String parameterDescription,
                                         ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    @PostConstruct
    @Override
    public void init() {
    }

    private RasipDataSourceServiceParameter getRasipDataSourceServiceParameter(ParameterContext parameterContext){
        final String tempUri = parameterContext.getSimpleParameters().get(
                RasipParameterDescription.TEMP_URI.parameterName);
        final String lightTempUri = parameterContext.getSimpleParameters().get(
                RasipParameterDescription.LIGHT_TEMP_URI.parameterName);
        final String lightLightUri = parameterContext.getSimpleParameters().get(
                RasipParameterDescription.LIGHT_LIGHT_URI.parameterName);

        return new RasipDataSourceServiceParameter() {
            @Override
            public String getTempUri() {
                return tempUri;
            }

            @Override
            public String getLightTempUri() {
                return lightTempUri;
            }

            @Override
            public String getLightLightUri() {
                return lightLightUri;
            }
        };
    }

    @Override
    public DataField[] getDataFields(ParameterContext parameterContext) throws HiperionException {

        RasipDataSourceServiceParameter rasipDataSourceServiceParameter =
                getRasipDataSourceServiceParameter(parameterContext);

        List<DataField> result = new ArrayList<DataField>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        result.add(getTempDataField(documentBuilderFactory,rasipDataSourceServiceParameter.getTempUri()));
        result.add(getLightTempDataField(documentBuilderFactory, rasipDataSourceServiceParameter.getLightTempUri()));
        result.add(getLightLightDataField(documentBuilderFactory,rasipDataSourceServiceParameter.getLightLightUri()));
        return result.toArray(new DataField[result.size()]);
    }

    private DataField getTempDataField(DocumentBuilderFactory documentBuilderFactory, String uri) throws HiperionException {

        final String fieldName = "rasip-temp";
        final DataType dataType = DataType.DOUBLE;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document rabbitXmlDocument = documentBuilder.parse(uri);
            rabbitXmlDocument.normalize();

            String value = rabbitXmlDocument.getDocumentElement().
                    getElementsByTagName("Temperature").item(0).getTextContent().trim();
            return new DataField(fieldName, dataType,
                    new String(value.getBytes(), DEFAULT_CHARSET));

        } catch (SAXException e) {
            throw new HiperionException(e);
        } catch (IOException e) {
            throw new HiperionException(e);
        } catch (ParserConfigurationException e) {
            throw new HiperionException(e);
        }
    }

    private DataField getLightTempDataField(DocumentBuilderFactory documentBuilderFactory, String uri) throws HiperionException {
        final String fieldName = "rasip-light-temp";
        final DataType dataType = DataType.DOUBLE;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document rabbitXmlDocument = documentBuilder.parse(uri);
            rabbitXmlDocument.normalize();

            String value = rabbitXmlDocument.getDocumentElement().
                    getElementsByTagName("podatak").item(0).getTextContent().trim();
            return new DataField(fieldName, dataType,
                    new String(value.getBytes(), DEFAULT_CHARSET));
        } catch (SAXException e) {
            throw new HiperionException(e);
        } catch (IOException e) {
            throw new HiperionException(e);
        } catch (ParserConfigurationException e) {
            throw new HiperionException(e);
        }
    }

    private DataField getLightLightDataField(DocumentBuilderFactory documentBuilderFactory, String uri) throws HiperionException {
        final String fieldName = "rasip-light-light";
        final DataType dataType = DataType.DOUBLE;

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document rabbitXmlDocument = documentBuilder.parse(uri);
            rabbitXmlDocument.normalize();

            String value = rabbitXmlDocument.getDocumentElement().
                    getElementsByTagName("podatak").item(0).getTextContent().trim();
            return new DataField(fieldName, dataType,
                    new String(value.getBytes(), DEFAULT_CHARSET));
        } catch (SAXException e) {
            throw new HiperionException(e);
        } catch (IOException e) {
            throw new HiperionException(e);
        } catch (ParserConfigurationException e) {
            throw new HiperionException(e);
        }
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = RasipParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (RasipParameterDescription rasipParameterDescription : RasipParameterDescription.values()) {
            result[i++] = new ParameterInfo(rasipParameterDescription.parameterName,
                    rasipParameterDescription.parameterDescription,
                    rasipParameterDescription.parameterType);
        }
        return result;
    }

    @Override
    public void dispose() {
    }
}
