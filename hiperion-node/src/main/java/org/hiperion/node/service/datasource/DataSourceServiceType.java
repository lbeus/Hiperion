package org.hiperion.node.service.datasource;

import org.hiperion.common.enums.EnumString;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.node.service.datasource.camera.CameraDataSourceService;
import org.hiperion.node.service.datasource.http.HttpDataSourceService;
import org.hiperion.node.service.datasource.rasip.RasipDataSourceService;
import org.hiperion.node.service.datasource.waspmote.WaspmoteDataSourceService;
import org.hiperion.node.service.datasource.yahoo.weather.YahooWeatherDataSourceService;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 15:09
 */
public enum DataSourceServiceType implements EnumString {
    HTTP_CLIENT("http-client", "Generic HTTP client.") {
        @Override
        public Class<? extends DataSourceService> getDataSourceServiceClassType() {
            return HttpDataSourceService.class;
        }
    },
    CAMERA("camera", "Local USB and integrated cameras.") {
        @Override
        public Class<? extends DataSourceService> getDataSourceServiceClassType() {
            return CameraDataSourceService.class;
        }
    },
    RASIP_SENSORS("rasip-sensors", "Local RASIP sensors: light, temperature, rabbit.") {
        @Override
        public Class<? extends DataSourceService> getDataSourceServiceClassType() {
            return RasipDataSourceService.class;
        }
    },
    WASPMOTE("waspmote", "Waspmote device data source."){
        @Override
        public Class<? extends DataSourceService> getDataSourceServiceClassType() {
            return WaspmoteDataSourceService.class;
        }
    },
    YAHOO_WEATHER("yahoo-weather", "Yahoo weather RSS feed."){
        @Override
        public Class<? extends DataSourceService> getDataSourceServiceClassType() {
            return YahooWeatherDataSourceService.class;
        }
    };

    private final String value;
    private final String description;

    private DataSourceServiceType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public abstract Class<? extends DataSourceService> getDataSourceServiceClassType();

    public static DataSourceServiceType getDataSourceServiceType(String value) throws HiperionException {
        for (DataSourceServiceType nodeDataSourceServiceType : DataSourceServiceType.values()) {
            if (nodeDataSourceServiceType.getValue().equals(value)) {
                return nodeDataSourceServiceType;
            }
        }
        throw new HiperionException("Data source service with name " + value + " not found.");
    }
}
