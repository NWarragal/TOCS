package sample;


import java.nio.charset.Charset;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        int a = 100;
        byte[] n = new byte[10];
        for (int i = 0; i < 10; i++){
            n[i] = (byte) a;
            a++;
            int b = n[i] & 0xFF;
            System.out.println(b);
        }
        String k = new String(n, Charset.forName("UTF-8"));//чистейший эксперимент и баловство со скан кодами
        System.out.println(k);

        String v = "GletsГлец1\n";
        System.out.println(v);

        byte[] j = v.getBytes();//преобразование строки в массив байт
        System.out.println(Arrays.toString(j));
        System.out.println("\n");

        byte[] u = new byte[5];//управление размерами разбиваемой на пакеты строки
        for (int e = 0; e < u.length; e++){
            u[e] = 0;
        }
        int o = 0;

        for (int i = 0; i < j.length; i++){
            u[o] = j[i];
            o++;
            if (o == u.length || (i + 1) == j.length){
                o = 0;
                System.out.println(Arrays.toString(u));
                StringBuilder sb = new StringBuilder();
                for (byte d : u) {
                    sb.append(String.format("0x%02X ", d));
                }
                System.out.println(sb.toString());
                System.out.println("\n");
                for (int e = 0; e < u.length; e++){
                    u[e] = 0;
                }
            }
        }

        String m = new String(j, Charset.forName("UTF-8"));//эта штукень возвращает все что было в байтах в изначальный вид
        System.out.println(m);

        StringBuilder sb = new StringBuilder();//эта волшебная штука нужна для вывода в консоль в шеснадцатеричном виде пакетов
        for (byte d : j) {
            sb.append(String.format("0x%02X ", d));//как один из вариантов вывода шесннадцатеричных цифр
        }
        System.out.println(sb.toString());//она идет досель
        byte[] bool = new byte[5];
        byte[] book = new byte[6];
        for (int i = 0; i < 5; i++){
            bool[i] = 0x01;
        }
        for (int i = 0; i < 6; i++){
            book[i] = 0x02;
        }
        System.out.println(Arrays.toString(bool));
        System.out.println(Arrays.toString(book));
        bool = book;
        System.out.println(Arrays.toString(bool));
    }
}
