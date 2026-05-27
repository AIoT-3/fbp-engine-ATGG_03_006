package com.fbp.engine.node;

import static org.junit.jupiter.api.Assertions.*;

import com.fbp.engine.message.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HumiditySensorNodeTest {

    @Test
    @DisplayName("습도 범위 및 필수 데이터 검증")
    void testHumidityDataGeneration() {
        double min = 30.0;
        double max = 90.0;
        String nodeId = "humi-01";
        HumiditySensorNode node = new HumiditySensorNode(nodeId, min, max);

        List<Message> receivedMessages = new ArrayList<>();
        TestSinkNode sink = new TestSinkNode("sink", receivedMessages);
        node.connect("out", sink, "in");

        Message triggerMsg = new Message(new HashMap<>());
        node.deliver(triggerMsg);

        assertFalse(receivedMessages.isEmpty());
        Message outMsg = receivedMessages.get(0);

        double humidity = (double) outMsg.get("humidity");
        assertTrue(humidity >= min && humidity <= max);

        assertTrue(outMsg.hasKey("sensorId"));
        assertTrue(outMsg.hasKey("humidity"));
        assertTrue(outMsg.hasKey("unit"));

        assertEquals(nodeId, outMsg.get("sensorId"));
    }

    @Test
    @DisplayName("트리거 횟수와 출력 메시지 수 일치 확인")
    void testTriggerCount() {
        HumiditySensorNode node = new HumiditySensorNode("humi-02", 30, 90);
        List<Message> receivedMessages = new ArrayList<>();
        TestSinkNode sink = new TestSinkNode("sink", receivedMessages);
        node.connect("out", sink, "in");

        Message triggerMsg = new Message(new HashMap<>());
        node.deliver(triggerMsg);
        node.deliver(triggerMsg);
        node.deliver(triggerMsg);

        assertEquals(3, receivedMessages.size());
    }

    private static class TestSinkNode extends com.fbp.engine.core.AbstractNode {
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