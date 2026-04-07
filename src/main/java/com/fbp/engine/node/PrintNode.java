package com.fbp.engine.node;

import com.fbp.engine.core.DefaultInputPort;
import com.fbp.engine.core.InputPort;
import com.fbp.engine.core.Node;
import com.fbp.engine.message.Message;

public class PrintNode implements Node {
    private String id;
    private InputPort inputPort;

    public PrintNode(String id) {
        this.id = id;
        this.inputPort = new DefaultInputPort(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        System.out.println("[Node" + id + "] Received" + message.getPayload());
    }

    public InputPort getInputPort() {
        return inputPort;
    }
}
