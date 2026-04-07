package com.fbp.engine.core;

import com.fbp.engine.message.Message;

import java.util.LinkedList;
import java.util.Queue;

public class Connection {
    private final String id;
    private final Queue<Message> buffer;
    private InputPort target;

    public Connection(String id) {
        this.id = id;
        this.buffer = new LinkedList<>();
    }

    public void deliver(Message message) {
        buffer.add(message);

        if (target != null && !buffer.isEmpty()) {
            Message msgDeliver = buffer.poll();
            target.receive(msgDeliver);
        }
    }

    public void setTarget(InputPort target) {
        this.target = target;
    }

    public int getBufferSize() {
        return buffer.size();
    }
}
