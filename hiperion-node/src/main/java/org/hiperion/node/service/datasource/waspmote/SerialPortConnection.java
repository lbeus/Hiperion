package org.hiperion.node.service.datasource.waspmote;

import gnu.io.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 11:15
 */
public class SerialPortConnection {

    private static final Logger LOGGER = Logger.getLogger(SerialPortConnection.class);

    private boolean open;
    private String serialPort;

    private OutputStream outputStream;
    private InputStream inputStream;
    private SerialPort sPort;
    private CommPortIdentifier portIdentifier;

    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;
    private int flowControlMode;

    /**
     * Creates a SerialPortConnection object and initialiazes variables passed in
     * as params.
     *
     * @param serialPort A SerialParameters object.
     */
    public SerialPortConnection(String serialPort) {
        this.open = false;
        this.serialPort = serialPort;

        // DEFAULT VALUES
        this.baudRate = 38400;
        this.dataBits = SerialPort.DATABITS_8;
        this.stopBits = SerialPort.STOPBITS_1;
        this.parity = SerialPort.PARITY_NONE;
        this.flowControlMode = -1;
    }

    public int getFlowControlMode() {
        return flowControlMode;
    }

    public void setFlowControlMode(int flowControlMode) {
        this.flowControlMode = flowControlMode;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    /**
     * Attempts to open a serial connection (9600 8N1). If it inputStream unsuccesfull
     * at any step it returns the port to a closed state, throws a
     * <code>SerialConnectionException</code>, and returns. <p/> Gives a
     * timeout of 30 seconds on the portOpen to allow other applications to
     * reliquish the port if have it open and no longer need it.
     */
    public boolean openConnection() {
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(serialPort);
        } catch (NoSuchPortException e) {
            LOGGER.error("Port doesn't exist : " + serialPort);
            return false;
        }

        if (portIdentifier.isCurrentlyOwned()) {
            LOGGER.error("Port owned by someone else");
            return false;
        }

        try {
            sPort = (SerialPort) portIdentifier.open("HiperionNodeSerialConnection", 30 * 1000);
            sPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);
            if (flowControlMode != -1) {
                sPort.setFlowControlMode(flowControlMode);
            }
        } catch (PortInUseException e) {
            LOGGER.error(e.getMessage(),e);
            return false;
        } catch (UnsupportedCommOperationException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        try {
            outputStream = sPort.getOutputStream();
            inputStream = sPort.getInputStream();
        } catch (IOException e) {
            sPort.close();
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        sPort.notifyOnDataAvailable(true);
        sPort.notifyOnBreakInterrupt(false);

        try {
            sPort.enableReceiveTimeout(30);
        } catch (UnsupportedCommOperationException e) {
        }
        open = true;
        return true;
    }

    /**
     * Close the port and clean up associated elements.
     */
    public void closeConnection() {
        if (!open) {
            return;
        }

        if (sPort != null) {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                System.err.println(e);
            }
            sPort.close();
        }
        open = false;
    }

    /**
     * Send a one second break signal.
     */
    public void sendBreak() {
        sPort.sendBreak(1000);
    }

    /**
     * Reports the open status of the port.
     *
     * @return true if port inputStream open, false if port inputStream closed.
     */
    public boolean isOpen() {
        return open;
    }

    public void addEventListener(SerialPortEventListener listener) {
        try {
            sPort.addEventListener(listener);
        } catch (TooManyListenersException e) {
            sPort.close();
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * Send a byte.
     */
    public void sendByte(int i) {
        try {
            outputStream.write(i);
        } catch (IOException e) {
            System.err.println("OutputStream write error: " + e);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
