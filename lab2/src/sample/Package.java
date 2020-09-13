package sample;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Package {
    private int destination = 1;
    private int source = 0;
    private boolean emerrror = false;
    private Boxes box;

    Package (Boxes box){
        this.box = box;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setEmerrror(boolean emerrror) {
        this.emerrror = emerrror;
    }

    public List<byte[]> GetToPackage (String message){
        byte[] bytemes = message.getBytes(StandardCharsets.UTF_8);
        byte[] pack = new byte[5];
        for (int i = 0; i < pack.length; i++){
            pack[i] = 0x03;
        }
        int j = 0, count = 0;
        List<byte[]> packager = new ArrayList<>();

        for (int i = 0; i < bytemes.length; i++){
            pack[j] = bytemes[i];
            j++;
            int flag = 10;
            if (j == pack.length || (i + 1) == bytemes.length){
                j = 0;

                packager.add(new byte[9]);

                packager.get(count)[0] = (byte) flag;
                packager.get(count)[1] = (byte)destination;
                packager.get(count)[2] = (byte)source;

                for (int d = 0; d < 5; d++){
                    packager.get(count)[d + 3] = pack[d];
                }

                if(emerrror) packager.get(count)[8] = 0x01;
                else  packager.get(count)[8] = 0x00;

                count++;

                for (int e = 0; e < pack.length; e++){
                    pack[e] = 0x03;
                }
            }
            if((i + 1) == bytemes.length){
                if(packager.get(count - 1)[7] != 3) {
                    packager.add(new byte[9]);

                    packager.get(count)[0] = (byte) flag;
                    packager.get(count)[1] = (byte) destination;
                    packager.get(count)[2] = (byte) source;

                    for (int d = 0; d < 5; d++) {
                        packager.get(count)[d + 3] = 0x03;
                    }

                    if (emerrror) packager.get(count)[8] = 0x01;
                    else packager.get(count)[8] = 0x00;
                }
            }
        }
        return packager;
    }

    public byte[] GetByteFromPackage (byte[] pakag){
        byte[] pack = new byte[5];
        for (int i = 0; i < pack.length; i++){
            pack[i] = 0x03;
        }
        if(pakag.length != 9){
            box.listdeb.add("ERROR: Problems with package");
            box.listDebug.setItems(box.listdeb);
        } else {
            if((pakag[1] & 0xFF) == source)  {
                if (pakag[8] == 0x01){
                    if(pakag[3] != 0x03) {
                        box.listdeb.add("ERROR: Simulation of error");
                        box.listDebug.setItems(box.listdeb);
                    }
                }else {
                    for (int d = 0; d < 5; d++) {
                        pack[d] = pakag[d + 3];
                    }
                }
            }
        }
        return pack;
    }

    public byte[] doByteStuffing(byte[] pack) {
        int i = 1;
        while (i < pack.length - 2) {
            byte currentByte = pack[i];

            if (currentByte == 0x7D)               // [7D] --> [7D6F]
            {
                byte[] neue = new byte[pack.length + 1];
                for (int j = 0; j < pack.length; j++) {
                    if (i > j){
                        neue[j] = pack[j];
                    }
                    if (i == j){
                        neue[j] = pack[j];
                        j++;
                        neue[j] = 0x6F;
                    }
                    if (i < j){
                        neue[j + 1] = pack[j];
                    }
                }
                pack = neue;
            }
            if (currentByte == 0x0A)               // [0A] --> [7D6E]
            {
                byte[] neue = new byte[pack.length + 1];
                for (int j = 0; j < pack.length; j++) {
                    if (i > j){
                        neue[j] = pack[j];
                    }
                    if (i == j){
                        neue[j] = 0x7D;
                        j++;
                        neue[j] = 0x6E;
                    }
                    if (i < j){
                        neue[j + 1] = pack[j];
                    }
                }
                pack = neue;
            }
            i++;
        }
        return pack;
    }

    public byte[] doDeByteStuffing(byte[] pack) {
        int i = 0;
        while (i < pack.length) {
            byte curr = pack[i];
            byte next = 0x00;
            if (i < pack.length - 1)
                next = pack[i + 1];

            if (curr ==  0x7D) {
                if (next == 0x6E)  // [7D6E]  --> [0A]
                {
                    byte[] neue = new byte[pack.length - 1];
                    for (int j = 0; j < pack.length; j++) {
                        if (i > j){
                            neue[j] = pack[j];
                        }
                        if (i == j){
                            neue[j] = 0x0A;
                            j++;
                        }
                        if (i + 1 == j){
                            j++;
                        }
                        if (i < j){
                            neue[j - 1] = pack[j];
                        }
                    }
                    pack = neue;
                }
                if (next == 0x6F)     // [7D6F] --> [7D]
                {
                    byte[] neue = new byte[pack.length - 1];
                    for (int j = 0; j < pack.length; j++) {
                        if (i > j){
                            neue[j] = pack[j];
                        }
                        if (i == j){
                            neue[j] = 0x7D;
                            j++;
                        }
                        if (i + 1 == j){
                            j++;
                        }
                        if (i < j){
                            neue[j - 1] = pack[j];
                        }
                    }
                    pack = neue;
                }
            }
            i++;
        }
        return pack;
    }

}
