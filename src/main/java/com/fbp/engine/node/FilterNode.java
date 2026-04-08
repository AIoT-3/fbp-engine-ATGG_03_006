package com.fbp.engine.node;

import com.fbp.engine.message.Message;

public class FilterNode extends AbstractNode {
    private final String key;
    private final double threshold;

    public FilterNode(String id, String key, double threshold) {
        super(id);
        this.key = key;
        this.threshold = threshold;

        addInputPort("in");
        addOutputPort("out");
    }

    @Override
    protected void onProcess(Message message) {
        Object value = message.get(key);

        if(value instanceof Number) {
            double numericValue = ((Number) value).doubleValue();

            if(numericValue > threshold) {
                send("out", message);
            }
        }
    }
}
