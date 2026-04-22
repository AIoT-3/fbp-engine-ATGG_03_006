package com.fbp.engine.core;

import java.util.*;

public class Flow {
    public enum State { RUNNING, STOPPED }
    private enum NodeStatus { UNVISITED, VISITING, VISITED }

    private final String id;
    private final Map<String, AbstractNode> nodes;
    private final List<Connection> connections;
    private State state;

    public Flow(String id) {
        this.id = id;
        this.nodes = new LinkedHashMap<>();
        this.connections = new ArrayList<>();
        this.state = State.STOPPED;
    }

    public Flow addNode(AbstractNode node) {
        nodes.put(node.getId(), node);
        return this;
    }

    public Flow connect(String sourceId, String sourcePortName, String targetId, String targetPortName) {
        AbstractNode sourceNode = nodes.get(sourceId);
        AbstractNode targetNode = nodes.get(targetId);

        if (sourceNode == null || targetNode == null) {
            throw new IllegalArgumentException("Invalid node ID: " + (sourceNode == null ? sourceId : targetId));
        }

        OutputPort sourcePort = sourceNode.getOutputPort(sourcePortName);
        InputPort targetPort = targetNode.getInputPort(targetPortName);

        if (sourcePort == null || targetPort == null) {
            throw new IllegalArgumentException("Invalid port name: " + (sourcePort == null ? sourcePortName : targetPortName));
        }

        String connId = String.format("%s:%s->%s:%s", sourceId, sourcePortName, targetId, targetPortName);
        Connection connection = new Connection(connId);

        sourcePort.connect(connection);
        targetPort.connect(connection);

        connections.add(connection);
        return this;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (nodes.isEmpty()) {
            errors.add("Error: No nodes registered in the flow.");
        }

        Map<String, NodeStatus> statusMap = new HashMap<>();
        for (String nodeId : nodes.keySet()) {
            statusMap.put(nodeId, NodeStatus.UNVISITED);
        }

        for (String nodeId : nodes.keySet()) {
            if (statusMap.get(nodeId) == NodeStatus.UNVISITED) {
                if (hasCycle(nodeId, statusMap)) {
                    errors.add("Error: Circular reference (Cycle) detected in the flow.");
                    break;
                }
            }
        }

        return errors;
    }

    private boolean hasCycle(String currentId, Map<String, NodeStatus> statusMap) {
        statusMap.put(currentId, NodeStatus.VISITING);

        for (Connection conn : connections) {
            if (conn.getId().startsWith(currentId + ":")) {
                String targetId = extractTargetId(conn.getId());
                NodeStatus targetStatus = statusMap.get(targetId);

                if (targetStatus == NodeStatus.VISITING) {
                    return true;
                }
                if (targetStatus == NodeStatus.UNVISITED && hasCycle(targetId, statusMap)) {
                    return true;
                }
            }
        }

        statusMap.put(currentId, NodeStatus.VISITED);
        return false;
    }

    private String extractTargetId(String connectionId) {
        return connectionId.split("->")[1].split(":")[0];
    }

    public void initialize() {
        nodes.values().forEach(AbstractNode::initialize);
        this.state = State.RUNNING;
    }

    public void shutdown() {
        nodes.values().forEach(AbstractNode::shutdown);
        this.state = State.STOPPED;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Collection<AbstractNode> getNodes() {
        return nodes.values();
    }

    public List<Connection> getConnections() {
        return Collections.unmodifiableList(connections);
    }
}