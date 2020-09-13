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
    private HammingCode ham = new HammingCode();

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
                            int s = 0;
                            int j = 9;
                            while (true){
                            byte[] pack = (serialPort.readBytes(1));
                            buff.add(pack[0]);
                                if (buff.size() > 1){
                                    if (buff.get((buff.size() - 1)) == 0x7D){
                                        j++;
                                    }
                                }
                                s++;
                                if (s == j){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    int errornum = 0;

                                    byte[] pack1 = new byte[pack.length];
                                    System.arraycopy(pack, 0, pack1, 0, pack.length);
                                    pack1 = ham.countSum(pack1);
                                    if (!(pack[pack.length - 1] == pack1[pack1.length - 1])){
                                        errornum = ham.getErrorNum(pack[pack.length - 1], pack1[pack1.length - 1]);
                                        pack = ham.correctionError(pack, errornum);
                                        Platform.runLater(() -> {
                                            box.listdeb.add("ERROR: Found mistake in package, auto correction is on");
                                            box.listDebug.setItems(box.listdeb);
                                        });
                                    }
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
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                    }
                    if (box.rts.isSelected()) {
                        if(serialPort.isCTS() && !serialPort.isDSR()) {
                            int s = 0;
                            int j = 9;
                            while (true){
                                byte[] pack = (serialPort.readBytes(1));
                                buff.add(pack[0]);
                                if (buff.size() > 1){
                                    if (buff.get((buff.size() - 1)) == 0x7D){
                                        j++;
                                    }
                                }
                                s++;
                                if (s == j){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    int errornum = 0;

                                    byte[] pack1 = new byte[pack.length];
                                    System.arraycopy(pack, 0, pack1, 0, pack.length);
                                    pack1 = ham.countSum(pack1);
                                    if (!(pack[pack.length - 1] == pack1[pack1.length - 1])){
                                        errornum = ham.getErrorNum(pack[pack.length - 1], pack1[pack1.length - 1]);
                                        pack = ham.correctionError(pack, errornum);
                                        Platform.runLater(() -> {
                                            box.listdeb.add("ERROR: Found mistake in package, auto correction is on");
                                            box.listDebug.setItems(box.listdeb);
                                        });
                                    }
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
                                    break;
                                }
                            }
                            if (flag) break;
                        }
                    }
                    if(box.none.isSelected()){
                        if(!serialPort.isCTS() && !serialPort.isDSR()) {
                            int s = 0;
                            int j = 9;
                            while (true){
                                byte[] pack = (serialPort.readBytes(1));
                                buff.add(pack[0]);
                                if (buff.size() > 1){
                                    if (buff.get((buff.size() - 1)) == 0x7D){
                                        j++;
                                    }
                                }
                                s++;
                                if (s == j){
                                    pack = new byte[buff.size()];
                                    for (int i = 0; i < buff.size(); i++){
                                        pack[i] = buff.get(i);
                                    }
                                    buff.clear();
                                    int errornum;
                                    byte[] pack1 = new byte[pack.length];
                                    System.arraycopy(pack, 0, pack1, 0, pack.length);
                                    pack1 = ham.countSum(pack1);
                                    if (!(pack[pack.length - 1] == pack1[pack1.length - 1])){
                                        errornum = ham.getErrorNum(pack[pack.length - 1], pack1[pack1.length - 1]);
                                        pack = ham.correctionError(pack, errornum);
                                        Platform.runLater(() -> {
                                            box.listdeb.add("ERROR: Found mistake in package, auto correction is on");
                                            box.listDebug.setItems(box.listdeb);
                                        });
                                    }
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
                                    break;
                                }
                            }
                            if (flag) break;
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
