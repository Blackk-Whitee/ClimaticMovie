package org.example.domain.models;

import java.time.Instant;

public class Event {
    private final String topic;
    private final String source;
    private final Instant timestamp;
    private final String rawData;

    public Event(String topic, String source, Instant timestamp, String rawData) {
        this.topic = topic;
        this.source = source;
        this.timestamp = timestamp;
        this.rawData = rawData;
    }

    public String getTopic() { return topic; }
    public String getSource() { return source; }
    public Instant getTimestamp() { return timestamp; }
    public String getRawData() { return rawData; }
}