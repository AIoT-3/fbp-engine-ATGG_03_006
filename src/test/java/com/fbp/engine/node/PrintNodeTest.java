package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PrintNodeTest {

    @Test
    @DisplayName("1. 포트 구성 확인")
    void testPortConfiguration() {
        PrintNode node = new PrintNode("p1");
        assertNotNull(node.getInputPort("in"), "입력 포트 'in'이 생성되어야 함");
    }

    @Test
    @DisplayName("2. process 정상 동작")
    void testProcessExecution() {
        PrintNode node = new PrintNode("p1");
        Message msg = new Message(Map.of("data", "hello"));

        assertDoesNotThrow(() -> node.process(msg));
    }

    @Test
    @DisplayName("3. AbstractNode 상속 확인")
    void testInheritance() {
        PrintNode node = new PrintNode("p1");
        assertTrue(node instanceof AbstractNode);
    }
}