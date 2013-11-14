package org.hiperion.node.service.datasource.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.exception.ParameterConversionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.datasource.DataSourceService;
import org.hiperion.node.util.DataConverter;
import org.hiperion.node.util.StringParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: iobestar
 * Date: 30.03.13.
 * Time: 23:23
 */
@Component
public class HttpDataSourceService implements DataSourceService {

    private final static Logger LOGGER = Logger.getLogger(HttpDataSourceService.class);

    private final static int MAX_BODY_SIZE = 1024000; // bytes
    private final static Charset DEFAULT_CHARSET = Charset.forName(System.getProperty("hiperion.node.default.charset"));

    private enum HttpParameterDescription {
        HOST("host", "Hostname for HTTP server.", ParameterType.SIMPLE),
        PORT("port", "Port number for HTTP server.", ParameterType.SIMPLE),
        URIS("uris", "Key-value pairs where key is name of output field and value is actual URI.", ParameterType.MAP);

        private final String parameterName;

        private final String parameterDescription;

        private final ParameterType parameterType;

        private HttpParameterDescription(String parameterName, String parameterDescription,
                                         ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }

        public String getParameterName() {
            return parameterName;
        }
    }

    private final HttpClient httpClient;
    private final StringParameterConverter stringParameterConverter;

    @Autowired
    public HttpDataSourceService(HttpClient httpClient, StringParameterConverter stringParameterConverter) {
        this.httpClient = httpClient;
        this.stringParameterConverter = stringParameterConverter;
    }

    @PostConstruct
    @Override
    public void init() {
        LOGGER.info("Data source service initialized.");
    }

    private HttpDataSourceServiceParameter getHttpDataSourceServiceParameter(ParameterContext parameterContext) throws ParameterConversionException {
        final String hostname = parameterContext.getSimpleParameters().
                get(HttpParameterDescription.HOST.getParameterName());

        final int port = stringParameterConverter.toInteger(parameterContext.getSimpleParameters().get(HttpParameterDescription.PORT.getParameterName()));

        final Map<String, String> dataFieldNameUriPairs = new HashMap<String, String>(
                parameterContext.getMapParameters().get(HttpParameterDescription.URIS.getParameterName()));

        return new HttpDataSourceServiceParameter() {
            @Override
            public String getHostName() {
                return hostname;
            }

            @Override
            public int getPort() {
                return port;
            }

            @Override
            public Map<String, String> getDataFieldNameUriPairs() {
                return dataFieldNameUriPairs;
            }
        };
    }

    @Override
    public DataField[] getDataFields(ParameterContext parameterContext)
            throws HiperionException {
        try {

            HttpDataSourceServiceParameter httpDataSourceServiceParameter =
                    getHttpDataSourceServiceParameter(parameterContext);

            String hostName = httpDataSourceServiceParameter.getHostName();
            int port = httpDataSourceServiceParameter.getPort();

            HttpHost httpHost = new HttpHost(hostName, port);
            List<DataField> fieldCollection = new LinkedList<DataField>();
            for (String dataFieldName : httpDataSourceServiceParameter.getDataFieldNameUriPairs().keySet()) {
                String uri = httpDataSourceServiceParameter.getDataFieldNameUriPairs().get(dataFieldName);
                HttpGet httpGet = new HttpGet(uri);
                HttpResponse response = httpClient.execute(httpHost, httpGet);
                if (isResponseOk(response)) {
                    LOGGER.warn("HTTP bad request. URI: " + uri);
                    continue;
                }
                if (responseOversize(response)) {
                    LOGGER.warn("HTTP response body oversize. URI: " + uri);
                    continue;
                }
                DataField dataField = convertToDataField(dataFieldName, response);
                fieldCollection.add(dataField);
            }
            return fieldCollection.toArray(new DataField[fieldCollection.size()]);
        } catch (IOException e) {
            throw new HiperionException(e);
        }
    }

    private boolean responseOversize(HttpResponse response) {
        return response.getEntity().getContentLength() > MAX_BODY_SIZE;
    }

    private boolean isResponseOk(HttpResponse response) {
        return response.getStatusLine().getStatusCode() != HttpStatus.SC_OK;
    }

    private DataField convertToDataField(String dataFieldName, HttpResponse response)
            throws IOException, HiperionException {

        DataType dataFieldType = resolveDataType(response);
        if (dataFieldType == DataType.BINARY_JPG || dataFieldType == DataType.BINARY_PNG ||
                dataFieldType == DataType.UNDEFINED) {
            String dataFieldValue = DataConverter.getBase64String(EntityUtils.toByteArray(response.getEntity()));
            return new DataField(dataFieldName, dataFieldType, dataFieldValue);
        }

        if (dataFieldType == DataType.JSON) {
            String dataFieldValue = EntityUtils.toString(response.getEntity());
            return new DataField(dataFieldName, dataFieldType, dataFieldValue);
        }

        if (dataFieldType == DataType.STRING) {
            ContentType contentType = ContentType.get(response.getEntity());
            Charset contentCharset = contentType.getCharset();
            if (null == contentCharset) {
                String dataFieldValue = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                return new DataField(dataFieldName, dataFieldType, dataFieldValue);
            }
            String dataFieldValue = EntityUtils.toString(response.getEntity());
            String defaultCharsetFieldValue = new String(dataFieldValue.getBytes(), DEFAULT_CHARSET);
            return new DataField(defaultCharsetFieldValue, dataFieldType, dataFieldValue);
        }

        throw new HiperionException("Error converting repository field.");
    }

    private DataType resolveDataType(HttpResponse response) throws IOException {

        ContentType contentType = ContentType.get(response.getEntity());

        if (contentType == ContentType.APPLICATION_JSON) {
            return DataType.JSON;
        }

        if (contentType == ContentType.TEXT_PLAIN) {
            return DataType.STRING;
        }

        final String jpgMimeType = "image/jpeg";
        if (contentType.getMimeType().equals(jpgMimeType)) {
            return DataType.BINARY_JPG;
        }

        final String pngMimeType = "image/png";
        if (contentType.getMimeType().equals(pngMimeType)) {
            return DataType.BINARY_PNG;
        }
        return DataType.UNDEFINED;
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = HttpParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (HttpParameterDescription httpParameterDescription : HttpParameterDescription.values()) {
            result[i++] = new ParameterInfo(httpParameterDescription.getParameterName(),
                    httpParameterDescription.parameterDescription, httpParameterDescription.parameterType);
        }
        return result;
    }

    @Override
    public void dispose() {
        httpClient.getConnectionManager().shutdown();
    }
}
