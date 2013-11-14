package org.hiperion.node.service.datasource.waspmote;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 11:50
 */
@Component
public class WaspmoteSerialPortListener implements SerialPortEventListener {

    private static final Logger LOGGER = Logger.getLogger(WaspmoteSerialPortListener.class);

    private final static String END_TAG = "!end!";
    private final static String GARBAGE_TAG = "R";

    private InputStream inputStream;
    private StringBuffer stringBuffer;

    private WaspmoteDataSourceService waspmoteDataSourceService;

    @Autowired
    public WaspmoteSerialPortListener(WaspmoteDataSourceService waspmoteDataSourceService,
                                      SerialPortConnection serialPortConnection) {
        if(serialPortConnection.isOpen()){
            serialPortConnection.addEventListener(this);
        }
        this.inputStream = serialPortConnection.getInputStream();
        this.stringBuffer = new StringBuffer();

        this.waspmoteDataSourceService = waspmoteDataSourceService;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (SerialPortEvent.DATA_AVAILABLE != serialPortEvent.getEventType()) {
            return;
        }
        try {
            int data;
            while ((data = inputStream.read()) > -1) {
                stringBuffer.append((char)data);
                if(stringBuffer.toString().contains(END_TAG)){
                    String waspmotePackage = stringBuffer.toString();
                    String outputData = waspmotePackage.split(GARBAGE_TAG)[1];
                    waspmoteDataSourceService.submitForProcessing(outputData);
                    stringBuffer = new StringBuffer();
                }
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
