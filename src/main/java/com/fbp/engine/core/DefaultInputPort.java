package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import com.fbp.engine.node.PrintNode;

public class DefaultInputPort implements InputPort {
    private String name;
    private Node owner;

    public DefaultInputPort(String name, Node owner) {
        this.name = name;
        this.owner = owner;
    }

    public DefaultInputPort(PrintNode printNode) {
    }

    public DefaultInputPort() {

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
}
