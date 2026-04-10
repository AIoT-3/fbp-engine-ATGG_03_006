package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import com.fbp.engine.node.PrintNode;

public class DefaultInputPort implements InputPort {
    private String name;
    private Node owner;
    private Connection connection;

    public DefaultInputPort(String name, Node owner) {
        this.name = name;
        this.owner = owner;
    }

    @Override
    public void receive(Message message) {
        if (owner != null) {
            owner.process(message);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void connect(Connection conn) {
        this.connection = conn;
        if (conn != null) {
            conn.setTarget(this);
        }
    }
}
