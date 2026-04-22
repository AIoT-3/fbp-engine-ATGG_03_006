package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogNode extends AbstractNode {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public LogNode(String id) {
        super(id);
        addInputPort("in");
        addOutputPort("out");
    }

    @Override
    protected void onProcess(Message message) {
        String timestamp = LocalTime.now().format(formatter);
        System.out.println(String.format("[%s][%s] %s", timestamp, id, message.getPayload()));
        send("out", message);
    }

    @Override
    public void deliver(Message m) {

    }
}
