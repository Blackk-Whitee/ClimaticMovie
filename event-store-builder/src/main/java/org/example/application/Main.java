package org.example.application;

import org.example.domain.repositories.EventRepository;
import org.example.domain.services.EventStorageService;
import org.example.infrastructure.storage.FileEventWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String brokerUrl = args[0];
        List<String> topics = Arrays.asList(args[1], args[2]);

        EventRepository repository = new FileEventWriter();
        EventStorageService service = new EventStorageService(repository);
        EventConsumer consumer = new EventConsumer(service, brokerUrl, topics);

        consumer.start();
        System.out.println("EventStoreBuilder iniciado...");
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}