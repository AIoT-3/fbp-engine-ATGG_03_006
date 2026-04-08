package com.fbp.engine.core;

import com.fbp.engine.message.Message;

import java.util.ArrayList;
import java.util.List;

public class DefaultOutputPort implements OutputPort{
    private final List<Connection> connections;

    public DefaultOutputPort() {
        this.connections = new ArrayList<>();
    }

    public void connect(Connection connection) {
        connections.add(connection);
    }

    public void send(Message message) {
        for (Connection connection : connections) {
            connection.deliver(message);
        }
    }
}