package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {

    @Test
    @DisplayName("1. delivery 후 target 수신")
    void testDeliverToTarget() {
        Connection connection = new Connection("c1");
        final Message[] received = {null};

        InputPort target = new InputPort() {
            @Override
            public void receive(Message message) {
                received[0] = message;
            }

            @Override
            public void deliver(Message message) {

            }

            @Override
            public String getName() { return "target-port"; }

            @Override
            public void connect(Connection conn) {

            }

            @Override
            public void setTarget(InputPort port) {

            }
        };

        connection.setTarget(target);
        Message message = new Message(Map.of("data", "test"));
        connection.deliver(message);

        assertNotNull(received[0]);
        assertEquals(message, received[0]);
     }

    @Test
    @DisplayName("2. target 미설정 시 동작")
    void testDeliverWithoutTarget() {
        Connection connection = new Connection("c1");
        Message message = new Message(Map.of("data", "buffer-test"));

        assertDoesNotThrow(() -> connection.deliver(message));
    }
    @Test
    @DisplayName("3. 버퍼 크기 확인")
    void testBufferSize() {
        Connection connection = new Connection("c1");

        connection.deliver(new Message(Map.of("id", 1)));
        connection.deliver(new Message(Map.of("id", 2)));

        assertEquals(2, connection.getBufferSize());
    }

    @Test
    @DisplayName("4. 다수 메시지 순서 보장")
    void testMessageOrder() {
        Connection connection = new Connection("c1");
        List<Object> result = new ArrayList<>();

        InputPort target = new InputPort() {
            @Override
            public void receive(Message message) {
                result.add(message.get("seq"));
            }

            @Override
            public void deliver(Message message) {

            }

            @Override
            public String getName() { return "order-test"; }

            @Override
            public void connect(Connection conn) {

            }

            @Override
            public void setTarget(InputPort port) {

            }
        };

        connection.setTarget(target);

        connection.deliver(new Message(Map.of("seq", 1)));
        connection.deliver(new Message(Map.of("seq", 2)));
        connection.deliver(new Message(Map.of("seq", 3)));

        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }
}
