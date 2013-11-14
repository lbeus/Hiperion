package org.hiperion.core.web.controller.service.sources;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 17.05.13.
 * Time: 14:32
 */
public class DataSourceRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private String description;
    private String latitude;
    private String longitude;

    public DataSourceRestBean() {
    }

    public DataSourceRestBean(String name, String description, String latitude, String longitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

