package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.concurrent.TimeUnit;

public class Port {
    private String namePort;
    private SerialPort serialPort;
    private Boxes box;
    private OutputCheck check;
    private Package pak;

    public Port(String namePort) {
        this.namePort = namePort;
    }

    public void OpenPort() throws Exception {
        serialPort = new SerialPort(namePort);
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.addEventListener(check = new OutputCheck(serialPort, box, pak));
        serialPort.setDTR(false);
        serialPort.setRTS(false);
    }

    public void setPak(Package pak) {
        this.pak = pak;
    }

    public void setBox(Boxes box) {
        this.box = box;
    }

    public void ClosePort() {
        try {
            check.setStopFlag();
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void SendByteArr(byte[] msg, String mode){
        if(mode.equals("RTS")) {
            try {
                serialPort.setRTS(true);
                serialPort.setDTR(false);
                for (int i = 0; i < msg.length; i++) {
                    serialPort.writeByte(msg[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        if(mode.equals("DTR")) {
            try {
                serialPort.setDTR(true);
                serialPort.setRTS(false);
                for (int i = 0; i < msg.length; i++) {
                    serialPort.writeByte(msg[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        if(mode.equals("none")) {
            try {
                serialPort.setRTS(false);
                serialPort.setDTR(false);
                for (int i = 0; i < msg.length; i++) {
                    serialPort.writeByte(msg[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
