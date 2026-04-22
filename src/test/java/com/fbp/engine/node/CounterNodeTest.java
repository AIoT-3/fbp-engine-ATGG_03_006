package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class CounterNodeTest {

    @Test
    @DisplayName("count 키 추가, 누적, 유지")
    void testCounterLogic() {
        // 내부 상태를 저장할 변수
        AtomicInteger counter = new AtomicInteger(0);

        TransformNode counterNode = new TransformNode("counter-1", msg -> {
            int currentCount = counter.incrementAndGet();
            Map<String, Object> newPayload = new java.util.HashMap<>(msg.getPayload());
            newPayload.put("count", currentCount);
            return new Message(newPayload);
        });

        Connection outConn = new Connection("out-c");
        counterNode.getOutputPort("out").connect(outConn);

        counterNode.process(new Message(Map.of("data", "first")));
        counterNode.process(new Message(Map.of("data", "second")));
        counterNode.process(new Message(Map.of("data", "third")));

        outConn.poll();
        outConn.poll();
        Message thirdMsg = outConn.poll();

        assertNotNull(thirdMsg);
        assertEquals(3, (Integer) thirdMsg.get("count"));
        assertEquals("third", thirdMsg.get("data"));
    }
}