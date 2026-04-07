package com.fbp.engine.core;

import com.fbp.engine.message.Message;

public class DefaultInputPort implements InputPort {
    private Node owner;

    public DefaultInputPort(Node owner) {
        this.owner = owner;
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
        return "";
    }
}
