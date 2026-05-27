package com.fbp.engine.node;

import static org.junit.jupiter.api.Assertions.*;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TemperatureSensorNodeTest {

    @Test
    @DisplayName("온도 범위 및 필수 데이터 키 검증")
    void testTemperatureDataValidation() {
        double min = 15.0;
        double max = 45.0;
        String nodeId = "temp-01";
        TemperatureSensorNode node = new TemperatureSensorNode(nodeId, min, max);

        List<Message> received = new ArrayList<>();
        TestSinkNode sink = new TestSinkNode("sink", received);
        node.connect("out", sink, "in");

        Message trigger = new Message(new HashMap<>());
        node.deliver(trigger);

        assertFalse(received.isEmpty());
        Message msg = received.get(0);

        double temp = (double) msg.get("temperature");
        assertTrue(temp >= min && temp <= max);

        assertTrue(msg.hasKey("sensorId"));
        assertTrue(msg.hasKey("temperature"));
        assertTrue(msg.hasKey("unit"));
        assertTrue(msg.hasKey("timestamp"));

        assertEquals(nodeId, msg.get("sensorId"));
    }

    @Test
    @DisplayName("트리거 횟수에 따른 메시지 생성 개수 검증")
    void testMessageGenerationCount() {
        TemperatureSensorNode node = new TemperatureSensorNode("temp-02", 20, 30);
        List<Message> received = new ArrayList<>();
        TestSinkNode sink = new TestSinkNode("sink", received);
        node.connect("out", sink, "in");

        Message trigger = new Message(new HashMap<>());
        node.deliver(trigger);
        node.deliver(trigger);
        node.deliver(trigger);

        assertEquals(3, received.size());
    }

    private static class TestSinkNode extends AbstractNode {
        private final List<Message> store;

        public TestSinkNode(String id, List<Message> store) {
            super(id);
            this.store = store;
            addInputPort("in");
        }

        @Override
        protected void onProcess(Message message) {
            store.add(message);
        }

        @Override
        public void deliver(Message m) {
            onProcess(m);
        }
    }
}