package com.fbp.engine.node;

import com.fbp.engine.core.DefaultOutputPort;
import com.fbp.engine.core.Node;
import com.fbp.engine.core.OutputPort;
import com.fbp.engine.message.Message;

public class GeneratorNode implements Node {
    private String id;
    private DefaultOutputPort outputPort;

    public GeneratorNode(String id) {
        this.id = id;
        this.outputPort = new DefaultOutputPort();
    }

    public DefaultOutputPort getOutputPort() {
        return outputPort;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        //빈 구현
    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    public void generate(String key, Object value) {
        Message message = new Message(java.util.Map.of(key, value));
        outputPort.send(message);
    }
}
