package org.ulpgc.climaticmovie.businessunit;

import org.ulpgc.climaticmovie.businessunit.application.controller.Control;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java -jar business-unit.jar <broker_url> <db_url>");
            System.err.println("Ejemplo: java -jar business-unit.jar tcp://localhost:61616 jdbc:sqlite:datamart.db");
            System.exit(1);
        }
        new Control(args[1]).start(args[0]);
    }
}
