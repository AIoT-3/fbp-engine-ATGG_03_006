package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import java.util.Map;

public class SplitNode extends AbstractNode {
    private final String key;
    private final double threshold;

    public SplitNode(String id, String key, double threshold) {
        super(id);
        this.key = key;
        this.threshold = threshold;
        addInputPort("in");
        addOutputPort("match");
        addOutputPort("mismatch");
    }

    @Override
    protected void onProcess(Message message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        double value = Double.parseDouble(payload.get(key).toString());

        if (value >= threshold) {
            send("match", message);
        } else {
            send("mismatch", message);
        }
    }

    @Override
    public void deliver(Message m) { }
}