package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.core.InputPort;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GeneratorNodeTest {

    @Test
    @DisplayName("GeneratorNode: 메시지 생성, 내용, 포트, 다수 호출 순서 검증")
    void testGeneratorNode() {
        // 3. OutputPort 조회
        GeneratorNode generator = new GeneratorNode("g1");
        assertNotNull(generator.getOutputPort(), "getOutputPort()가 null이 아니어야 함");

        List<Message> receivedMessages = new ArrayList<>();
        InputPort mockTarget = new InputPort() {
            @Override
            public void receive(Message message) {
                receivedMessages.add(message);
            }

            @Override
            public void deliver(Message message) {

            }

            @Override
            public String getName() { return "mock-target"; }

            @Override
            public void connect(Connection conn) {

            }

            @Override
            public void setTarget(InputPort port) {

            }
        };

        Connection connection = new Connection("c1");
        connection.setTarget(mockTarget);
        generator.getOutputPort().connect(connection);

        // 1. generate 메시지 생성
        generator.generate("temperature", 25.5);

        assertEquals(1, receivedMessages.size(), "메시지가 1개 전달되어야 함");
        Message firstMsg = receivedMessages.get(0);
        assertEquals(25.5, (Double) firstMsg.get("temperature"), "지정한 key-value가 포함되어야 함");

        // 4. 다수 generate 호출
        generator.generate("seq", 2);
        generator.generate("seq", 3);

        assertEquals(3, receivedMessages.size(), "총 3개의 메시지가 전달되어야 함");

        assertTrue(receivedMessages.get(0).hasKey("temperature"));
        assertEquals(2, (Integer) receivedMessages.get(1).get("seq"));
        assertEquals(3, (Integer) receivedMessages.get(2).get("seq"));
    }
}