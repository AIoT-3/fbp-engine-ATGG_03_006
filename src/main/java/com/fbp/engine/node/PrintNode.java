package com.fbp.engine.node;

import com.fbp.engine.core.Node;
import com.fbp.engine.message.Message;

public class PrintNode implements Node {
    private String id;

    public PrintNode(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        System.out.println("[" + id + "]" + message.getPayload());
    }
}
