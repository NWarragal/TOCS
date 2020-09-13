package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Test {

    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort("COM1");
        String message = "{Hi}";
        int i = 0;
        String letter = "";
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            for (i = 0; i < message.length(); i++) {
                serialPort.setRTS(true);
                if(serialPort.isCTS()) {
                    serialPort.setRTS(false);
                    serialPort.writeByte(message.getBytes()[i]);
                }
            }
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
        message = "";
        i = 0;
        try {
            //while (true) {
                while (true) {
                    if (serialPort.isDSR()) {
                        serialPort.setDTR(true);
                        if (serialPort.isDSR()) {
                            serialPort.setDTR(false);
                            message += (serialPort.readString(1));
                            letter = Character.toString(message.charAt(i));
                        }
                    }
                    if (letter.equals("}")) {
                        break;
                    }
                    i++;
                }
                System.out.println(message);
                //message = "";
                //i = 0;
            //}
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}