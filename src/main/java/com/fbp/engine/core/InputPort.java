package com.fbp.engine.core;

import com.fbp.engine.message.Message;

public interface InputPort {
    void receive(Message msgDeliver);

    void deliver(Message message);

    String getName();

    void connect(Connection conn);

    void setTarget(InputPort port);
}
