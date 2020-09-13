package sample;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class OutputCheck implements SerialPortEventListener {
    private String messageOutput = "";
    private Boxes box;
    private SerialPort serialPort;
    private boolean stopFlag = false;

    OutputCheck(SerialPort serialPort, Boxes box){
        this.serialPort = serialPort;
        this.box = box;
    }

    public void setStopFlag() {
        stopFlag = true;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        while (true) {
            String letter;
            try {
                while (true) {
                    if (box.dtr.isSelected()) {
//                        if (serialPort.isDSR()) {
//                            serialPort.setDTR(true);
//                            while(true){
//                                if(!serialPort.isDSR()) break;
//                            }
//                            serialPort.setDTR(false);
//                            letter = (serialPort.readString(1));
//                            if (letter.equals("\n")) {
//                                break;
//                            }
//                            messageOutput += letter;
//                        }
                        if (serialPort.isDSR()) {
                            letter = (serialPort.readString(1));
                            if (letter.equals("\n")) {
                                break;
                            }
                            messageOutput += letter;
                        }
                    }
                    if (box.rts.isSelected()) {
//                        if (serialPort.isCTS()) {
//                            serialPort.setRTS(true);
//                            while(true){
//                                if(!serialPort.isCTS()) break;
//                            }
//                            serialPort.setRTS(false);
//                            letter = (serialPort.readString(1));
//                            if (letter.equals("\n")) {
//                                break;
//                            }
//                            messageOutput += letter;
//                        }
                        if (serialPort.isCTS()) {
                            letter = (serialPort.readString(1));
                            if (letter.equals("\n")) {
                                break;
                            }
                            messageOutput += letter;
                        }
                    }
                    if(box.none.isSelected()){
                        if(!serialPort.isCTS() && !serialPort.isDSR()) {
                            letter = (serialPort.readString(1));
                            if (letter.equals("\n")) {
                                break;
                            }
                            messageOutput += letter;
                        }
                    }
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
            if(!messageOutput.equals("")) {
                Platform.runLater(() -> {
                    box.list.add(messageOutput);
                    box.listOutput.setItems(box.list);
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageOutput = "";
            if (stopFlag) break;
        }
    }
}
