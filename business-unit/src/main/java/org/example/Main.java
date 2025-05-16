package org.example;

import org.example.aplication.controller.Control;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Main <broker-url>");
            System.out.println("Ejemplo: java Main tcp://localhost:61616");
            return;
        }

        new Control().start(args[0]);
    }
}