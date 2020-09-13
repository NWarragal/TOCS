package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.concurrent.TimeUnit;

public class Port {
    private String namePort;
    private SerialPort serialPort;
    private Boxes box;
    private OutputCheck check;

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
        serialPort.addEventListener(check = new OutputCheck(serialPort, box));
        serialPort.setDTR(false);
        serialPort.setRTS(false);
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

    public void SendString(String msg, String mode){
        if(mode.equals("RTS")) {
            try {
                serialPort.setRTS(true);
                serialPort.setDTR(false);
//                for (int i = 0; i < msg.length(); i++) {
//                    while(true){
//                        if (serialPort.isCTS()) break;
//                    }
//                    serialPort.setRTS(false);
//                    while(true){
//                        if (!serialPort.isCTS()) break;
//                    }
//                    if(!serialPort.isCTS()){
//                        serialPort.writeByte(msg.getBytes()[i]);
//                    }
//                    serialPort.setRTS(true);
//                }
//                serialPort.setRTS(false);
                for (int i = 0; i < msg.length(); i++) {
                    serialPort.writeByte(msg.getBytes()[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        if(mode.equals("DTR")) {
            try {
                serialPort.setDTR(true);
                serialPort.setRTS(false);
//                for (int i = 0; i < msg.length(); i++) {
//                    while(true){
//                        if (serialPort.isDSR()) break;
//                    }
//                    serialPort.setDTR(false);
//                    while(true){
//                        if (!serialPort.isDSR()) break;
//                    }
//                    if(!serialPort.isDSR()){
//                        serialPort.writeByte(msg.getBytes()[i]);
//                    }
//                    serialPort.setDTR(true);
//                }
//                serialPort.setDTR(false);
                for (int i = 0; i < msg.length(); i++) {
                    serialPort.writeByte(msg.getBytes()[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        if(mode.equals("none")) {
            try {
                serialPort.setRTS(false);
                serialPort.setDTR(false);
                for (int i = 0; i < msg.length(); i++) {
                    serialPort.writeByte(msg.getBytes()[i]);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
