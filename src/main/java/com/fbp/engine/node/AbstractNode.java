package com.fbp.engine.node;

import com.fbp.engine.core.*;
import com.fbp.engine.message.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNode implements Node {
    protected String id;
    private Map<String, InputPort> inputPorts;
    private Map<String, OutputPort> outputPorts;

    public AbstractNode(String id) {
        this.id = id;
        this.inputPorts = new HashMap<>();
        this.outputPorts = new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        System.out.println("[" + id + "] processing message: " + message.getId());
        onProcess(message);
        System.out.println("[" + id + "] processing completed.");
    }

    protected abstract void onProcess(Message message);

    protected void addInputPort(String name) {
        inputPorts.put(name, new DefaultInputPort(name, this));
    }

    protected void addOutputPort(String name) {
        outputPorts.put(name, new DefaultOutputPort());
    }

    public InputPort getInputPort(String name) {
        return inputPorts.get(name);
    }

    public OutputPort getOutputPort(String name) {
        return outputPorts.get(name);
    }

    protected void send(String portName, Message message) {
        OutputPort port = outputPorts.get(portName);
        if (port != null) {
            port.send(message);
        }
    }

    @Override
    public void initialize() {
        System.out.println("[" + id + "] Initializing...");
    }

    @Override
    public void shutdown() {
        System.out.println("[" + id + "] Shutting down...");
    }
}
