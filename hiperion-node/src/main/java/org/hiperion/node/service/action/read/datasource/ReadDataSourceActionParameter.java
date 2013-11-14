package org.hiperion.node.service.action.read.datasource;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 17:59
 */
public interface ReadDataSourceActionParameter {

    String getPushCollectorId();

    String getDataSourceId();

    String getPushUri();

    String getDataSourceServiceId();

    String getDescriptionFieldName();

    String getDescriptionFieldValue();
}
