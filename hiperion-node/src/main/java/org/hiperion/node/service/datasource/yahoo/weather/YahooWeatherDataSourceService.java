package org.hiperion.node.service.datasource.yahoo.weather;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.datasource.DataSourceService;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: iobestar
 * Date: 23.06.13.
 * Time: 16:28
 */
@Component
public class YahooWeatherDataSourceService implements DataSourceService{

    private static final Logger LOGGER = Logger.getLogger(YahooWeatherDataSourceService.class);

    private enum YahooWeatherParameterDescription {
        FORECAST_RSS_URI("forecast-rss-uri", "Yahoo weather forecast RSS URI.", ParameterType.SIMPLE);

        private final String parameterName;

        private final String parameterDescription;

        private final ParameterType parameterType;

        private YahooWeatherParameterDescription(String parameterName, String parameterDescription,
                                          ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    private final static String WIND_TAG = "yweather:wind";
    private final static String ATMOSPHERE_TAG = "yweather:atmosphere";
    private final static String ASTRONOMY_TAG = "yweather:astronomy";

    @PostConstruct
    @Override
    public void init() {
    }

    private YahooWeatherDataSourceServiceParameter getYahooWeatherDataSourceServiceParameter(
            ParameterContext parameterContext){
        final String forecastRssUri = parameterContext.getSimpleParameters().get(
                YahooWeatherParameterDescription.FORECAST_RSS_URI.parameterName);

        return new YahooWeatherDataSourceServiceParameter() {
            @Override
            public String getForecastRssURI() {
                return forecastRssUri;
            }
        };
    }

    @Override
    public DataField[] getDataFields(ParameterContext parameterContext) throws HiperionException {

        YahooWeatherDataSourceServiceParameter yahooWeatherDataSourceServiceParameter =
                getYahooWeatherDataSourceServiceParameter(parameterContext);

        try {
            List<DataField> result = new ArrayList<DataField>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document yahooXmlDocument = documentBuilder.parse(
                    yahooWeatherDataSourceServiceParameter.getForecastRssURI());
            result.add(getTemperature(yahooXmlDocument));
            result.add(getWindDirection(yahooXmlDocument));
            result.add(getWindSpeed(yahooXmlDocument));
            result.add(getHumidity(yahooXmlDocument));
            result.add(getVisibility(yahooXmlDocument));
            result.add(getPressure(yahooXmlDocument));
            result.add(getSunrise(yahooXmlDocument));
            result.add(getSunset(yahooXmlDocument));
            return result.toArray(new DataField[result.size()]);
        } catch (SAXException e) {
            throw new HiperionException(e);
        } catch (IOException e) {
            throw new HiperionException(e);
        } catch (ParserConfigurationException e) {
            throw new HiperionException(e);
        }
    }

    private DataField getTemperature(Document yahooXmlDocument){
        Element windElement = (Element) yahooXmlDocument.getElementsByTagName(WIND_TAG).item(0);
        String temperature = windElement.getAttribute("chill");
        DataField dataField = new DataField("temperature", DataType.DOUBLE,temperature);
        return dataField;
    }

    private DataField getWindDirection(Document yahooXmlDocument){
        Element windElement = (Element) yahooXmlDocument.getElementsByTagName(WIND_TAG).item(0);
        String direction = windElement.getAttribute("direction");
        DataField dataField = new DataField("wind-direction", DataType.DOUBLE,direction);
        return dataField;
    }

    private DataField getWindSpeed(Document yahooXmlDocument){
        Element windElement = (Element) yahooXmlDocument.getElementsByTagName(WIND_TAG).item(0);
        String windSpeed = windElement.getAttribute("speed");
        DataField dataField = new DataField("wind-speed", DataType.DOUBLE,windSpeed);
        return dataField;
    }

    private DataField getHumidity(Document yahooXmlDocument){
        Element atmosphereElement = (Element) yahooXmlDocument.getElementsByTagName(ATMOSPHERE_TAG).item(0);
        String humidity = atmosphereElement.getAttribute("humidity");
        DataField dataField = new DataField("humidity", DataType.DOUBLE,humidity);
        return dataField;
    }

    private DataField getVisibility(Document yahooXmlDocument){
        Element atmosphereElement = (Element) yahooXmlDocument.getElementsByTagName(ATMOSPHERE_TAG).item(0);
        String visibility = atmosphereElement.getAttribute("visibility");
        DataField dataField = new DataField("visibility", DataType.DOUBLE,visibility);
        return dataField;
    }

    private DataField getPressure(Document yahooXmlDocument){
        Element atmosphereElement = (Element) yahooXmlDocument.getElementsByTagName(ATMOSPHERE_TAG).item(0);
        String pressure = atmosphereElement.getAttribute("pressure");
        DataField dataField = new DataField("pressure", DataType.DOUBLE,pressure);
        return dataField;
    }

    private DataField getSunrise(Document yahooXmlDocument){
        Element astronomyElement = (Element) yahooXmlDocument.getElementsByTagName(ASTRONOMY_TAG).item(0);
        String sunrise = astronomyElement.getAttribute("sunrise");
        DataField dataField = new DataField("sunrise", DataType.STRING,sunrise);
        return dataField;
    }

    private DataField getSunset(Document yahooXmlDocument){
        Element atmosphereElement = (Element) yahooXmlDocument.getElementsByTagName(ASTRONOMY_TAG).item(0);
        String sunset = atmosphereElement.getAttribute("sunset");
        DataField dataField = new DataField("sunset", DataType.STRING,sunset);
        return dataField;
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = YahooWeatherParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (YahooWeatherParameterDescription yahooWeatherParameterDescription : YahooWeatherParameterDescription.values()) {
            result[i++] = new ParameterInfo(yahooWeatherParameterDescription.parameterName,
                    yahooWeatherParameterDescription.parameterDescription,
                    yahooWeatherParameterDescription.parameterType);
        }
        return result;    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
