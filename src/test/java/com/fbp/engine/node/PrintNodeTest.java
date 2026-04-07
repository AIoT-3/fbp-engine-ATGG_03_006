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

        // process 호출 시 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> node.process(message));
    }

    @Test
    @DisplayName("3. Node 인터페이스 구현")
    void testNodeInterfaceImplementation() {
        PrintNode printNode = new PrintNode("node-01");

        // Node 타입 변수에 대입 가능한지 확인 (다형성)
        assertTrue(printNode instanceof Node);

        Node node = printNode; // 컴파일 에러가 없으면 통과
        assertNotNull(node);
    }
}