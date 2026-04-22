package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNode implements Node {
    protected String id;
    protected Map<String, InputPort> inputPorts;
    protected Map<String, OutputPort> outputPorts;

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
        onProcess(message);
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

    public InputPort getInputPort() {
        return inputPorts.get("in");
    }

    public OutputPort getOutputPort() {
        return outputPorts.get("out");
    }

    protected void send(String portName, Message message) {
        OutputPort port = outputPorts.get(portName);
        if (port != null) {
            port.send(message);
        }
    }

    public void connect(String outputPortName, AbstractNode targetNode, String inputPortName) {
        OutputPort outPort = this.getOutputPort(outputPortName);
        InputPort inPort = targetNode.getInputPort(inputPortName);

        if (outPort != null && inPort != null) {
            Connection connection = new Connection(id + "-" + targetNode.getId());
            connection.setTarget(inPort);
            outPort.connect(connection);
            inPort.connect(connection);
        }
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }

    public void start() {
        initialize();
    }

    public abstract void deliver(Message m);
}