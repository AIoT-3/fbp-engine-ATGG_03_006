package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class AbstractNodeTest {

    static class TestNode extends AbstractNode {
        boolean onProcessCalled = false;
        public TestNode(String id) { super(id); }
        @Override
        protected void onProcess(Message message) { onProcessCalled = true; }

        @Override
        public void deliver(Message m) {

        }

        public void testAddInput(String name) { addInputPort(name); }
        public void testAddOutput(String name) { addOutputPort(name); }
        public void testSend(String name, Message m) { send(name, m); }
    }

    @Test
    @DisplayName("1. getId 반환")
    void testGetId() {
        TestNode node = new TestNode("node-1");
        assertEquals("node-1", node.getId());
    }

    @Test
    @DisplayName("2. addInputPort 등록")
    void testAddInputPort() {
        TestNode node = new TestNode("node-1");
        node.testAddInput("in");
        assertNotNull(node.getInputPort("in"));
    }

    @Test
    @DisplayName("3. addOutputPort 등록")
    void testAddOutputPort() {
        TestNode node = new TestNode("node-1");
        node.testAddOutput("out");
        assertNotNull(node.getOutputPort("out"));
    }

    @Test
    @DisplayName("4. 미등록 포트 조회")
    void testGetNonExistentPort() {
        TestNode node = new TestNode("node-1");
        assertNull(node.getInputPort("unknown"));
    }

    @Test
    @DisplayName("5. process -> onProcess 호출")
    void testProcessCallsOnProcess() {
        TestNode node = new TestNode("node-1");
        node.process(new Message(Map.of()));
        assertTrue(node.onProcessCalled, "process()를 호출하면 onProcess()가 실행되어야 함");
    }

    @Test
    @DisplayName("6. send로 메시지 전달")
    void testSendThroughOutputPort() {
        TestNode node = new TestNode("node-1");
        node.testAddOutput("out");

        Connection conn = new Connection("c1");
        node.getOutputPort("out").connect(conn);

        node.testSend("out", new Message(Map.of("data", "test")));
        assertEquals(1, conn.getBufferSize(), "send() 호출 시 연결된 커넥션으로 전달되어야 함");
    }
}