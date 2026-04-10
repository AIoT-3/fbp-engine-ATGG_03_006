package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.core.InputPort;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class LogNodeTest {

    @Test
    @DisplayName("1. 메시지 통과 전달")
    void testMessagePassThrough() {
        LogNode logNode = new LogNode("log1");
        Connection conn = new Connection("c1");
        logNode.getOutputPort("out").connect(conn);

        Message original = new Message(Map.of("data", "important"));
        logNode.process(original);

        Message passed = conn.poll();
        assertNotNull(passed);
        assertEquals(original.getPayload(), passed.getPayload(), "전달된 메시지 내용이 원본과 동일해야 함");
    }

    @Test
    @DisplayName("2. 중간 삽입 가능")
    void testIntermediateInsertion() {
        LogNode logNode = new LogNode("log-mid");
        Connection connToB = new Connection("to-b");

        final Message[] receivedAtB = {null};
        InputPort mockPortB = new InputPort() {
            @Override public void receive(Message m) { receivedAtB[0] = m; }
            @Override public String getName() { return "port-b"; }
            @Override public void connect(Connection c) { }
        };

        connToB.setTarget(mockPortB);

        logNode.getOutputPort("out").connect(connToB);

        Message msg = new Message(Map.of("val", 100));
        logNode.process(msg);

        Message polledMsg = connToB.poll();
        if (polledMsg != null) {
            mockPortB.receive(polledMsg);
        }

        assertNotNull(receivedAtB[0], "중간의 LogNode를 거쳐서 최종 노드 B까지 도달해야 함");
        assertEquals(100, (Integer) receivedAtB[0].get("val"));
    }
}