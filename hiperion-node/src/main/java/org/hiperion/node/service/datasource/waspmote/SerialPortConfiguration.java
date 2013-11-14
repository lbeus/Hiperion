package org.hiperion.node.service.datasource.waspmote;

import gnu.io.SerialPort;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 11:55
 */
public enum SerialPortConfiguration {
    DATABITS_5{
        @Override
        public int value() {
            return SerialPort.DATABITS_5;
        }
    },DATABITS_6 {
        @Override
        public int value() {
            return SerialPort.DATABITS_6;
        }
    },DATABITS_7 {
        @Override
        public int value() {
            return SerialPort.DATABITS_7;
        }
    },DATABITS_8 {
        @Override
        public int value() {
            return SerialPort.DATABITS_8;
        }
    },PARITY_NONE{
        @Override
        public int value() {
            return SerialPort.PARITY_NONE;
        }
    },PARITY_ODD {
        @Override
        public int value() {
            return SerialPort.PARITY_ODD;
        }
    },PARITY_EVEN{
        @Override
        public int value() {
            return SerialPort.PARITY_EVEN;
        }
    },PARITY_MARK{
        @Override
        public int value() {
            return SerialPort.PARITY_MARK;
        }
    },PARITY_SPACE{
        @Override
        public int value() {
            return SerialPort.PARITY_SPACE;
        }
    },STOPBITS_1{
        @Override
        public int value() {
            return SerialPort.STOPBITS_1;
        }
    },STOPBITS_2{
        @Override
        public int value() {
            return SerialPort.STOPBITS_2;
        }
    },STOPBITS_1_5{
        @Override
        public int value() {
            return SerialPort.STOPBITS_1_5;
        }
    },FLOWCONTROL_NONE{
        @Override
        public int value() {
            return SerialPort.FLOWCONTROL_NONE;
        }
    },FLOWCONTROL_RTSCTS_IN{
        @Override
        public int value() {
            return SerialPort.FLOWCONTROL_RTSCTS_IN;
        }
    },FLOWCONTROL_RTSCTS_OUT{
        @Override
        public int value() {
            return SerialPort.FLOWCONTROL_RTSCTS_OUT;
        }
    },FLOWCONTROL_XONXOFF_IN{
        @Override
        public int value() {
            return SerialPort.FLOWCONTROL_XONXOFF_IN;
        }
    },FLOWCONTROL_XONXOFF_OUT{
        @Override
        public int value() {
            return SerialPort.FLOWCONTROL_XONXOFF_OUT;
        }
    };

    private SerialPortConfiguration(){
    }

    public abstract int value();
}
