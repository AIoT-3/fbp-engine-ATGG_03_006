package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class DelayNodeTest {

    @Test
    @DisplayName("1. 지연 후 전달 및 2. 내용 보존 확인")
    void testDelayLogic() throws InterruptedException {
        long delay = 500;
        DelayNode delayNode = new DelayNode("d1", delay);
        Connection outConn = new Connection("out-c");
        delayNode.getOutputPort("out").connect(outConn);

        Message msg = new Message(Map.of("info", "stay"));
        long startTime = System.currentTimeMillis();

        delayNode.process(msg);

        assertNull(outConn.poll());

        Thread.sleep(delay + 100);

        Message result = outConn.poll();
        long duration = System.currentTimeMillis() - startTime;

        assertNotNull(result);
        assertTrue(duration >= delay, "지연 시간보다 빨리 전달되어서는 안 됨");

        assertEquals("stay", result.get("info"));

        delayNode.shutdown();
    }
}