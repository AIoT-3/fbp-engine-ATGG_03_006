package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultOutputPortTest {

    @Test
    @DisplayName("1. 단일 Connection 전달")
    void testSingleConnection() {
        DefaultOutputPort outputPort = new DefaultOutputPort();
        Connection connection = new Connection("c1");
        outputPort.connect(connection);

        outputPort.send(new Message(Map.of("data", "test")));

        assertEquals(1, connection.getBufferSize());
    }
    @Test
    @DisplayName("2. 다중 Connection 전달 (1:N)")
    void testMultipleConnections() {
        DefaultOutputPort outputPort = new DefaultOutputPort();
        Connection c1 = new Connection("c1");
        Connection c2 = new Connection("c2");

        outputPort.connect(c1);
        outputPort.connect(c2);

        outputPort.send(new Message(Map.of("data", "broadcast")));

        assertEquals(1, c1.getBufferSize());
        assertEquals(1, c2.getBufferSize());
    }

    @Test
    @DisplayName("3. Connection 미연결 시")
    void testNoConnection() {
        DefaultOutputPort outputPort = new DefaultOutputPort();

        assertDoesNotThrow(() -> outputPort.send(new Message(Map.of())));
    }
}
