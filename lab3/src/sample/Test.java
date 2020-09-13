package sample;

import java.util.Arrays;
import java.util.BitSet;

public class Test {
    private static int[] mas_num = new int[]{1, 2, 4, 8, 16, 32, 64, 128};

    public static void main(String[] args) {
        byte[] msold = new byte[]{0x3C, 0x3C, 0x3C, 0x3C, 0x3C, 0x3C, 0x3C, 0x3C};
        byte[] ms = delLactByte(msold);
        BitSet realbit = BitSet.valueOf(ms);
        BitSet virtbit = new BitSet();
        BitSet bitsum = new BitSet();

        int real = ms.length * 8;

        System.out.println(real);
        int countcontrbits = 0;
        for (int i = 0; i < mas_num.length; i++){
            if (mas_num[i] <= real)countcontrbits++;
        }
        System.out.println(countcontrbits);
        System.out.println(real + countcontrbits - 1);

        virtbit = toHammingViev(realbit, real, countcontrbits);

        String n = "";
        int counter = 0;

        for (int i = real - 1; i > -1; i--) {
            counter++;
            int m = 0;
            if(realbit.get(i)) m = 1;
            if(m == 1) n += "1";
            else n += "0";
            if (counter == 8){
                counter = 0;
                n += " ";
            }
        }
        System.out.println(n);

        n = "";

        for (int i = real + countcontrbits - 1; i > -1; i--) {
            int m = 0;
            if(virtbit.get(i)) m = 1;
            if(m == 1) n += "1";
            else n += "0";
        }
        System.out.println(n);


        bitsum = contrSum(virtbit, real, countcontrbits);

        n = "";

        for (int i = 0; i < 8; i++) {
            int m = 0;
            if(bitsum.get(i)) m = 1;
            if(m == 1) n += "1";
            else n += "0";
        }
        System.out.println(n);

        realbit = fromHammingViev(virtbit, real, countcontrbits);

        n = "";
        counter = 0;

        for (int i = real - 1; i > -1; i--) {
            counter++;
            int m = 0;
            if(realbit.get(i)) m = 1;
            if(m == 1) n += "1";
            else n += "0";
            if (counter == 8){
                counter = 0;
                n += " ";
            }
        }
        System.out.println(n);

        System.out.println(Arrays.toString(ms));
        byte[] c = realbit.toByteArray();
        System.out.println(Arrays.toString(c));

        byte[] sum = bitsum.toByteArray();
        if (sum.length == 0) System.out.println("huyilo");
    }

    private static BitSet contrSum (BitSet virtbit, int iter, int countcontrbits){ //убрать статик при переносе!
        BitSet bitsum = new BitSet();
        int virtual = 1;
        boolean flagwait = true;
        for (int j = 0; j < 8; j++) {
            int bitcheck = mas_num[j];
            int bitsumm = 0;
            for (int i = iter + countcontrbits - 1; i > -1; i--) {
            //for (int i = 0; i < iter + countcontrbits; i++) {
                if (virtual >= bitcheck){
                    for (int a = 0; a < bitcheck; a++){
                        if (flagwait){
                            if (virtual >= iter + countcontrbits + 1) break;
                            if(virtbit.get(iter + countcontrbits - virtual)){
                                bitsumm += 1;
                            }
                        }
                        virtual++;
                    }
                    flagwait = !flagwait;
                } else {
                    virtual++;
                }
            }
            if ((bitsumm % 2) == 1) bitsum.flip(j);
            flagwait = true;
            virtual = 1;
        }

        return bitsum;
    }

    private static BitSet toHammingViev (BitSet realbit, int iter, int countcontrbits){
        BitSet virtbit = new BitSet();
        int virtual = 1, real = iter;
        boolean flag = false;
        // в вид для хемминга
        for (int i = iter + countcontrbits - 1; i > -1; i--){//внимание! я работаю с старших битов, и нумерация избыточного бита будет считаться по виртуальному адресу с начала отсчета!
            for (int j = 0; j < mas_num.length; j++){
                if(virtual == mas_num[j]) flag = true;
            }
            if(!flag){
                real--;
                if(realbit.get(real)){
                    virtbit.flip(i);
                }
                if (real == 0) break;
            }
            virtual++;
            flag = false;
        }

        return virtbit;
    }

    private static BitSet fromHammingViev (BitSet virtbit, int iter, int countcontrbits){
        BitSet realbit = new BitSet();
        //вернуть из вида для хемминга обратно
        int virtual = 1;
        int real = iter;
        boolean flag = false;
        for (int i = iter + countcontrbits - 1; i > -1; i--){
            for (int j = 0; j < mas_num.length; j++){
                if(virtual == mas_num[j]) flag = true;
            }
            if(!flag){
                real--;
                if(virtbit.get(i)){
                    realbit.flip(real);
                }
                if (real == 0) break;
            }
            virtual++;
            flag = false;
        }
        return realbit;
    }

    private static byte[] delLactByte(byte[] ms){
        byte[] newms = new byte[ms.length -1];
        for (int i = 0; i < newms.length; i++){
            newms[i] = ms[i];
        }
        return newms;
    }
}
