package com.fbp.engine.message;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Message {
    private final UUID id; //메세지가 어떻게 흘러가는지 추적하기 위해 사용
    private final Map<String, Object> payload;
    private final Instant timestamp;

    public Message(Map<String, Object> payload) {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.payload = Collections.unmodifiableMap(new HashMap<>(payload));
    }

    private Message(UUID id, Map<String, Object> newPayload, Instant timestamp) {
        this.id = id;
        this.payload = Collections.unmodifiableMap(new HashMap<>(newPayload));
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) payload.get(key);
    }

    public boolean hasKey(String key) {
        return payload.containsKey(key);
    }

    public Message withEntry(String key, Object value){
        Map<String, Object> newPayload = new HashMap<>(this.payload);
        newPayload.put(key, value);
        return new Message(this.id, newPayload, this.timestamp);
    }

    public Message withoutKey(String key) {
        Map<String, Object> newPayload = new HashMap<>(this.payload);
        newPayload.remove(key);
        return new Message(this.id, newPayload, this.timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", playload=" + payload +
                ", timestamp=" + timestamp +
                '}';
    }
}
