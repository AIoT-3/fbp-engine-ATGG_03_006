package com.fbp.engine.node;

import static org.junit.jupiter.api.Assertions.*;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TransformNodeTest {

    @Test
    @DisplayName("변환 정상 동작")
    void testTransformSuccess() {
        TransformNode node = new TransformNode("t1", (m) -> {
            int val = (int) ((Map) m.getPayload()).get("value");
            return new Message(Map.of("value", val * 2));
        });

        List<Message> results = new ArrayList<>();
        node.connect("out", new AbstractNode("mock") {
            @Override protected void onProcess(Message m) { results.add(m); } // 여기서 담아야 함
            @Override public void deliver(Message m) { }
        }, "in");

        node.process(new Message(Map.of("value", 10)));

        assertEquals(1, results.size());
        assertEquals(20, ((Map) results.get(0).getPayload()).get("value"));
    }

    @Test
    @DisplayName("null 반환 시 미전달")
    void testTransformNullReturn() {
        TransformNode node = new TransformNode("t2", (m) -> null);
        List<Message> results = new ArrayList<>();
        node.connect("out", new AbstractNode("mock") {
            @Override protected void onProcess(Message m) { results.add(m); }
            @Override public void deliver(Message m) {
                results.add(m);
            }
        }, "in");

        node.process(new Message(Map.of("value", 10)));

        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("원본 메시지 불변")
    void testImmutability() {
        Map<String, Object> payload = Map.of("value", 10);
        Message originalMessage = new Message(payload);

        TransformNode node = new TransformNode("t3", (m) -> new Message(Map.of("value", 999)));
        node.process(originalMessage);

        assertEquals(10, ((Map) originalMessage.getPayload()).get("value"));
    }
}