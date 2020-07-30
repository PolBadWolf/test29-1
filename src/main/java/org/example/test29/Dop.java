package org.example.test29;

import java.util.function.Consumer;

public class Dop {
    public void start() {
        mid(t -> pusk1((String) t));
        mid(t -> pusk2((String) t));
        System.exit(0);
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
