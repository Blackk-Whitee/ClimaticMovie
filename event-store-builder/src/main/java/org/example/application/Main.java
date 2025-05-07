package org.example.application;

import org.example.domain.repositories.EventRepository;
import org.example.domain.services.EventStorageService;
import org.example.infrastructure.storage.FileEventWriter;

public class Main {
    public static void main(String[] args) {
        String brokerUrl = args[0];

        EventRepository repository = new FileEventWriter();
        EventStorageService service = new EventStorageService(repository);
        EventConsumer consumer = new EventConsumer(service, brokerUrl);

        consumer.start();
        System.out.println("EventStoreBuilder iniciado...");
    }
}