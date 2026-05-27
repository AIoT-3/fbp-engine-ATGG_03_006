package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
    private final String id;
    private BlockingQueue<Message> buffer;
    private InputPort target;

    public Connection(String id) {
        this(id, 100);
    }

    public Connection(String id, int capacity) {
        this.id = id;
        this.buffer = new LinkedBlockingQueue<>(capacity);
    }

    public String getId() {
        return id;
    }

    public void deliver(Message message) {
        try {
            buffer.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        if (this.target != null) {
            this.target.receive(message);
        }
    }

    public Message poll() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void setTarget(InputPort target) {
        this.target = target;
    }

    public int getBufferSize() {
        return buffer.size();
    }
}