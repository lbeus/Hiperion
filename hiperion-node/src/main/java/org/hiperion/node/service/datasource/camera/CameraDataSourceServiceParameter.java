package org.hiperion.node.service.datasource.camera;

import org.hiperion.node.service.datasource.DataSourceServiceParameter;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 30.03.13.
 * Time: 23:27
 */
public interface CameraDataSourceServiceParameter extends DataSourceServiceParameter {
    int getDeviceId();

    int getHeight();

    int getWidth();

    String getDataFieldName();
}
