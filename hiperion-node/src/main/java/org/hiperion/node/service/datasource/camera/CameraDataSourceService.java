package org.hiperion.node.service.datasource.camera;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.exception.ParameterConversionException;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.DataType;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.util.StringParameterConverter;
import org.hiperion.node.service.datasource.DataSourceService;
import org.hiperion.node.util.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: iobestar
 * Date: 30.03.13.
 * Time: 23:29
 */

@Component
public class CameraDataSourceService implements DataSourceService {

    private final static Logger LOGGER = Logger.getLogger(CameraDataSourceService.class);

    private enum CameraParameterDescription {
        DEVICE_ID("device-id", "Camera device id as integer.", ParameterType.SIMPLE),
        WIDTH("width", "Picture width in pixels as integer.", ParameterType.SIMPLE),
        HEIGHT("height", "Picture height in pixels as integer.", ParameterType.SIMPLE),
        DATA_FIELD_NAME("data-field-name", "Name of output data field as string.", ParameterType.SIMPLE);

        private final String parameterName;
        private final String parameterDescription;
        private final ParameterType parameterType;

        private CameraParameterDescription(String parameterName, String parameterDescription,
                                           ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    private final OpenCVPictureService openCVPictureService;

    private final StringParameterConverter stringParameterConverter;

    @Autowired
    public CameraDataSourceService(OpenCVPictureService openCVPictureService,
                                   StringParameterConverter stringParameterConverter) {
        this.openCVPictureService = openCVPictureService;
        this.stringParameterConverter = stringParameterConverter;
    }

    @PostConstruct
    @Override
    public void init() {
        LOGGER.info("Data source service initialized.");
    }

    private CameraDataSourceServiceParameter getCameraDataSourceServiceParameter(ParameterContext parameterContext)
            throws ParameterConversionException {

        final int deviceId = stringParameterConverter.toInteger(
                parameterContext.getSimpleParameters().
                        get(CameraParameterDescription.DEVICE_ID.parameterName));

        final int height = stringParameterConverter.toInteger(
                parameterContext.getSimpleParameters().
                        get(CameraParameterDescription.HEIGHT.parameterName));

        final int width = stringParameterConverter.toInteger(
                parameterContext.getSimpleParameters().
                        get(CameraParameterDescription.WIDTH.parameterName));

        final String dataFieldName = parameterContext.getSimpleParameters().get(
                CameraParameterDescription.DATA_FIELD_NAME.parameterName);

        return new CameraDataSourceServiceParameter() {
            @Override
            public int getDeviceId() {
                return deviceId;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public String getDataFieldName() {
                return dataFieldName;
            }
        };
    }

    @Override
    public DataField[] getDataFields(ParameterContext parameterContext)
            throws HiperionException {
        try {

            CameraDataSourceServiceParameter cameraDataSourceServiceParameter =
                    getCameraDataSourceServiceParameter(parameterContext);
            byte[] imageInByte = openCVPictureService.takePicture(
                    cameraDataSourceServiceParameter.getDeviceId(),
                    cameraDataSourceServiceParameter.getWidth(),
                    cameraDataSourceServiceParameter.getHeight()
            );

            DataField[] dataFields = new DataField[1];
            String fieldName = cameraDataSourceServiceParameter.getDataFieldName();
            DataType dataType = DataType.BINARY_PNG;
            if(openCVPictureService.getImageType() == OpenCVPictureService.ImageType.JPG){
                dataType = DataType.BINARY_JPG;
            }
            String value = DataConverter.getBase64String(imageInByte);
            dataFields[0] = new DataField(fieldName, dataType, value);

            return dataFields;
        } catch (HiperionException e) {
            throw e;
        }
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = CameraParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (CameraParameterDescription cameraParameterDescription : CameraParameterDescription.values()) {
            result[i++] = new ParameterInfo(cameraParameterDescription.parameterName,
                    cameraParameterDescription.parameterDescription,
                    cameraParameterDescription.parameterType);
        }
        return result;
    }

    @Override
    public void dispose() {
        LOGGER.info("Data source service disposed.");
    }
}
