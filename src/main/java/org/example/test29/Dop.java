package org.example.test29;

import org.example.test29.sql.MyBlob;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.function.Consumer;

public class Dop {
    public void start() {
        mid(t -> pusk1((String) t));
        mid(t -> pusk2((String) t));
        // ----
        Class classData1 = Timestamp.class;
        Class classData2 = byte[].class;
        String str1 = classData1.getName();
        String[] ss = classData1.getName().split("\\.");
        String str2 = ss[ss.length - 1];
        str1 = " 0";
    }

    void pusk1(String t) {
        System.out.println(t + " - pusk 1");
    }

    void pusk2(String t) {
        System.out.println(t + " - pusk 2");
    }


    void mid(Consumer metod) {
        metod.accept("Hello");
    }
}
