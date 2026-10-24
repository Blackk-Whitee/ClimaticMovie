package org.ulpgc.climaticmovie.eventstorebuilder;

import org.ulpgc.climaticmovie.eventstorebuilder.application.EventConsumer;
import org.ulpgc.climaticmovie.eventstorebuilder.domain.services.EventStorageService;
import org.ulpgc.climaticmovie.eventstorebuilder.infrastructure.storage.FileEventWriter;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("Uso: java -jar event-store-builder.jar <broker_url> <topic> [<topic>...]");
            System.err.println("Ejemplo: java -jar event-store-builder.jar tcp://localhost:61616 Weather movies.Trending");
            System.exit(1);
        }
        String brokerUrl = args[0];
        List<String> topics = Arrays.asList(args).subList(1, args.length);

        EventStorageService service = new EventStorageService(new FileEventWriter());
        EventConsumer consumer = new EventConsumer(service, brokerUrl, topics);

        consumer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
        System.out.println("EventStoreBuilder iniciado. Escuchando: " + topics);
        Thread.currentThread().join();
    }
}
