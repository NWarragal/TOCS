package sample;

import java.util.BitSet;

public class HammingCode {
    private static int[] mas_num = new int[]{1, 2, 4, 8, 16, 32, 64, 128};

    public byte[] countSum(byte[] msold){
        byte[] ms = delLactByte(msold);
        BitSet realbit = BitSet.valueOf(ms);
        BitSet virtbit = new BitSet();
        BitSet bitsum = new BitSet();
        int real = ms.length * 8;

        int countcontrbits = 0;
        for (int i = 0; i < mas_num.length; i++){
            if (mas_num[i] <= real)countcontrbits++;
        }

        virtbit = toHammingViev(realbit, real, countcontrbits);
        bitsum = calculation(virtbit, real, countcontrbits);
        byte[] sum = bitsum.toByteArray();
        if (sum.length  == 0){
            byte zero = 0x00;
            ms = addSumToEnd(msold, zero);
        } else {
            ms = addSumToEnd(msold, sum[0]);
        }
        return ms;
    }

    public int getErrorNum (byte mainsum, byte secsum){
        byte[] main = new byte[]{mainsum};
        byte[] secondary = new  byte[]{secsum};
        BitSet first = BitSet.valueOf(main);
        BitSet second = BitSet.valueOf(secondary);
        BitSet differences = new BitSet();
        int num = 0;
        for (int i = 0; i < 8; i++){
            if (first.get(i) != second.get(i)) differences.flip(i);
        }
        for (int i = 0; i < 8; i++){
            if (differences.get(i)) num += mas_num[i];
        }
        return num;
    }

    public byte[] correctionError(byte[] msold, int numerr){
        int virtual = 1;
        byte[] ms = delLactByte(msold);
        BitSet realbit = BitSet.valueOf(ms);
        BitSet virtbit = new BitSet();
        BitSet resbit = new BitSet();
        int real = ms.length * 8;

        int countcontrbits = 0;
        for (int i = 0; i < mas_num.length; i++){
            if (mas_num[i] <= real)countcontrbits++;
        }
        virtbit = toHammingViev(realbit, real, countcontrbits);
        for (int i = real + countcontrbits - 1; i > -1; i--){
            if (virtual == numerr){
                virtbit.flip(i);
            }
            virtual++;
        }
        resbit = fromHammingViev(virtbit, real, countcontrbits);
        byte[] msnew = resbit.toByteArray();
        byte zero = 0x00;
        System.arraycopy(msnew, 0, msold, 0, msnew.length);
        msnew = addSumToEnd(msold, zero);
        return msnew;
    }

    public byte[] generateError (byte[] msold){
        BitSet arrbit = BitSet.valueOf(msold);
        int real = msold.length * 8;
        arrbit.flip((int) (Math.random() * real));
        byte[] ms = arrbit.toByteArray();
        if (ms.length != msold.length){
            byte zero = 0x00;
            switch (msold.length - ms.length){
                case 1:
                    ms = addSumToEnd(ms, zero);
                    break;
                default:
                    for(int i = 0; i < msold.length - ms.length; i++){
                        ms = addSumToEnd(ms, zero);
                    }
                    break;
            }
        }
        return ms;
    }

    private BitSet calculation(BitSet virtbit, int iter, int countcontrbits){
        BitSet bitsum = new BitSet();
        int virtual = 1;
        boolean flagwait = true;
        for (int j = 0; j < 8; j++) {
            int bitcheck = mas_num[j];
            int bitsumm = 0;
            for (int i = iter + countcontrbits - 1; i > -1; i--) {
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

    private BitSet toHammingViev(BitSet realbit, int iter, int countcontrbits){
        BitSet virtbit = new BitSet();
        int virtual = 1, real = iter;
        boolean flag = false;
        for (int i = iter + countcontrbits - 1; i > -1; i--){
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

    private BitSet fromHammingViev (BitSet virtbit, int iter, int countcontrbits){
        BitSet realbit = new BitSet();
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

    private byte[] delLactByte(byte[] ms){
        byte[] newms = new byte[ms.length -1];
        for (int i = 0; i < newms.length; i++){
            newms[i] = ms[i];
        }
        return newms;
    }

    private byte[] addSumToEnd (byte[] ms, byte sum){
        ms[ms.length - 1] = sum;
        return ms;
    }
}
