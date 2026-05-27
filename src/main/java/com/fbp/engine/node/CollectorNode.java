package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;

import java.util.ArrayList;
import java.util.List;

public class CollectorNode extends AbstractNode {
    private final List<Message> collected;

    public CollectorNode(String id) {
        super(id);
        this.collected = new ArrayList<>();
        addInputPort("in");
    }

    @Override
    public void onProcess(Message message) {
        if (message != null) {
            collected.add(message);
        }
    }

    @Override
    public void deliver(Message m) {

    }

    public List<Message> getCollected() {
        return collected;
    }
}
