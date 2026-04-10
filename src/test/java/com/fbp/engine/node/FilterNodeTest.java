package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FilterNodeTest {

    @Test
    @DisplayName("1. 조건 만족 -> send 호출, 2. 조건 미달 -> 차단")
    void testFilterLogic() {
        FilterNode filter = new FilterNode("f1", "temp", 30.0);
        Connection conn = new Connection("c1");

        filter.getOutputPort("out").connect(conn);

        filter.process(new Message(Map.of("temp", 35.0)));
        assertEquals(1, conn.getBufferSize());

        filter.process(new Message(Map.of("temp", 25.0)));
        assertEquals(1, conn.getBufferSize(), "미달 메시지는 차단되어 버퍼가 유지되어야 함");
    }

    @Test
    @DisplayName("3. 포트 구성 확인")
    void testPortConfiguration() {
        FilterNode filter = new FilterNode("f1", "temp", 30.0);
        assertNotNull(filter.getInputPort("in"));
        assertNotNull(filter.getOutputPort("out"));
    }
}