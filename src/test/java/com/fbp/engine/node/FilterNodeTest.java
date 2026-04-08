package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FilterNodeTest {

    @Test
    @DisplayName("FilterNode 테스트")
    void testFilterNode() {
        // "temperature" 키의 값이 30.0 이상인 것만 통과시키는 필터 생성
        FilterNode filter = new FilterNode("f1", "temperature", 30.0);

        // 결과 확인용 Connection 연결
        Connection conn = new Connection("c1");
        filter.getOutputPort().connect(conn);

        // 1. 조건 만족 시 통과
        filter.process(new Message(Map.of("temperature", 35.0)));
        assertEquals(1, conn.getBufferSize(), "35.0은 통과하여 버퍼가 1이 되어야 함");

        // 2. 조건 미달 시 차단
        filter.process(new Message(Map.of("temperature", 25.0)));
        assertEquals(1, conn.getBufferSize(), "25.0은 차단되어 버퍼 크기가 유지되어야 함");

        // 3. 경계값 처리
        filter.process(new Message(Map.of("temperature", 30.0)));
        assertEquals(2, conn.getBufferSize(), "30.0은 이상(>=) 조건이므로 통과해야 함");

        // 4. 키 없는 메시지
        assertDoesNotThrow(() -> {
            filter.process(new Message(Map.of("humidity", 50.0)));
        });
        assertEquals(2, conn.getBufferSize(), "키가 없는 메시지는 전달되지 않아야 함");
    }
}
