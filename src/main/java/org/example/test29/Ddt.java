package org.example.test29;

public class Ddt {
    public int tik;
    public int dat;

    public Ddt(int tik, int dat) {
        this.tik = tik;
        this.dat = dat;
    }

    private static void int2bytes(int source, int sourceLenght, byte[] target, int index) {
        for (int i = 0; i < sourceLenght; i++) {
            target[i + index] = (byte) (source & 0xff);
            source >>= 8;
        }
    }

    public static byte[] toBytes(Ddt[] mass) {
        byte[] bytes = new byte[6 * mass.length];
        for (int i = 0; i < mass.length; i++) {
            int2bytes(mass[i].tik, 4, bytes, (6 * i) + 0);
            int2bytes(mass[i].dat, 2, bytes, (6 * i) + 4);
        }
        return bytes;
    }

}
