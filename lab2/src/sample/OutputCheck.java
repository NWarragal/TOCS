package sample;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class OutputCheck implements SerialPortEventListener {
    private String messageOutput = "";
    private Boxes box;
    private SerialPort serialPort;
    private boolean stopFlag = false;
    private Package pak;

    OutputCheck(SerialPort serialPort, Boxes box, Package pak){
        this.serialPort = serialPort;
        this.box = box;
        this.pak = pak;
    }

    public void setStopFlag() {
        stopFlag = true;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        while (true) {
            List<byte[]> packager = new ArrayList<>();
            List<Byte> buff = new ArrayList<>();
            int count = 0;
            boolean flag = false;
            try {
                while (true) {
                    if (box.dtr.isSelected()) {
                        if(!serialPort.isCTS() && serialPort.isDSR()) {
                            byte[] pack = (serialPort.readBytes(1));
                            buff.add(pack[0]);
                            if(buff.size() >= 9){
                                if (buff.get((buff.size() - 1)) == 0x00 || buff.get((buff.size() - 1)) == 0x01){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    pack = pak.doDeByteStuffing(pack);
                                    pack = pak.GetByteFromPackage(pack);
                                    packager.add(new byte[5]);
                                    for (int i = 0; i < 5; i++){
                                        packager.get(count)[i] = pack[i];
                                    }
                                    for (int i = 0; i < 5; i++){
                                        if (packager.get(count)[i] == 3) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    count++;
                                    if (flag) break;
                                }
                            }
                        }
                    }
                    if (box.rts.isSelected()) {
                        if(serialPort.isCTS() && !serialPort.isDSR()) {
                            byte[] pack = (serialPort.readBytes(1));
                            buff.add(pack[0]);
                            if(buff.size() >= 9){
                                if (buff.get((buff.size() - 1)) == 0x00 || buff.get((buff.size() - 1)) == 0x01){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    pack = pak.doDeByteStuffing(pack);
                                    pack = pak.GetByteFromPackage(pack);
                                    packager.add(new byte[5]);
                                    for (int i = 0; i < 5; i++){
                                        packager.get(count)[i] = pack[i];
                                    }
                                    for (int i = 0; i < 5; i++){
                                        if (packager.get(count)[i] == 3) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    count++;
                                    if (flag) break;
                                }
                            }
                        }
                    }
                    if(box.none.isSelected()){
                        if(!serialPort.isCTS() && !serialPort.isDSR()) {
                            byte[] pack = (serialPort.readBytes(1));
                            buff.add(pack[0]);
                            if(buff.size() >= 9){
                                if (buff.get((buff.size() - 1)) == 0x00 || buff.get((buff.size() - 1)) == 0x01){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    pack = pak.doDeByteStuffing(pack);
                                    pack = pak.GetByteFromPackage(pack);
                                    packager.add(new byte[5]);
                                    for (int i = 0; i < 5; i++){
                                        packager.get(count)[i] = pack[i];
                                    }
                                    for (int i = 0; i < 5; i++){
                                        if (packager.get(count)[i] == 3) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    count++;
                                    if (flag) break;
                                }
                            }
                        }
                    }
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
            int size = packager.size() * 5;
            byte[] str = new byte[size];
            size = 0;
            for (int i = 0; i < packager.size(); i++){
                for (int j = 0; j < 5; j++){
                    if (packager.get(i)[j] != 3){
                        str[size] = packager.get(i)[j];
                        size++;
                    }
                }
            }
            if (str[0] != 0) {
                messageOutput = new String(str, Charset.forName("UTF-8"));
                if (!messageOutput.equals("")) {
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
            }
            messageOutput = "";
            if (stopFlag) break;
        }
    }
}
