package com.fbp.engine.node;

import com.fbp.engine.core.Node;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PrintNodeTest {

    @Test
    @DisplayName("1. getId 반환")
    void testGetId() {
        String expectedId = "node-01";
        PrintNode node = new PrintNode(expectedId);
        assertEquals(expectedId, node.getId());
    }

    @Test
    @DisplayName("2. process 정상 동작")
    void testProcessExecution() {
        PrintNode node = new PrintNode("test-node");
        Message message = new Message(Map.of("data", "hello"));

        assertDoesNotThrow(() -> node.process(message));
    }

    @Test
    @DisplayName("3. Node 인터페이스 구현")
    void testNodeInterfaceImplementation() {
        PrintNode printNode = new PrintNode("node-01");

        assertTrue(printNode instanceof Node);

        Node node = printNode;
        assertNotNull(node);
    }

    @Test
    @DisplayName("4. InputPort 조회")
    void testGetInputPort() {
        PrintNode node = new PrintNode("p1");

        assertNotNull(node.getInputPort(), "PrintNode는 생성 시 InputPort를 가지고 있어야 함");
    }

    @Test
    @DisplayName("5. InputPort를 통한 수신")
    void testReceiveThroughPort() {
        PrintNode node = new PrintNode("p1");
        Message message = new Message(Map.of("data", "test-message"));

        assertDoesNotThrow(() -> {
            node.getInputPort().receive(message);
        }, "InputPort로 메시지를 수신했을 때 오류 없이 처리되어야 함");
    }
}
