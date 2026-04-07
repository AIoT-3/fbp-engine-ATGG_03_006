package com.fbp.engine.node;

import com.fbp.engine.core.DefaultInputPort;
import com.fbp.engine.core.DefaultOutputPort;
import com.fbp.engine.core.InputPort;
import com.fbp.engine.core.Node;
import com.fbp.engine.message.Message;


public class FilterNode implements Node {
    private final String id;
    private final String key;
    private final double threshold;
    private final InputPort inputPort;
    private final DefaultOutputPort outputPort;

    public FilterNode(String id, String key, double threshold) {
        this.id = id;
        this.key = key;
        this.threshold = threshold;
        this.inputPort = new DefaultInputPort(this);
        this.outputPort = new DefaultOutputPort();
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        if (message.hasKey(key)) {
            Object value = message.get(key);

            if (value instanceof Number) {
                double doubleValue = ((Number) value).doubleValue();

                if (doubleValue >= threshold) {
                    outputPort.send(message);
                }
            }
        }
    }

    public InputPort getInputPort() {
        return this.inputPort;
    }

    public DefaultOutputPort getOutputPort() {
        return this.outputPort;
    }
}
