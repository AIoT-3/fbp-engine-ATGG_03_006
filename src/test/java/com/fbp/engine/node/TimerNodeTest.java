package com.fbp.engine.node;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TimerNodeTest {

    @Test
    @DisplayName("1. initialize 후 메시지 생성")
    void testInitializeStartsGeneration() throws InterruptedException {
        TimerNode timer = new TimerNode("t1", 100);
        Connection conn = new Connection("c1");
        timer.getOutputPort("out").connect(conn);

        timer.initialize();
        Thread.sleep(350);
        timer.shutdown();

        assertTrue(conn.getBufferSize() >= 3, "0.35초 대기 후 최소 3개 이상의 메시지가 생성되어야 함");
    }

    @Test
    @DisplayName("2. tick 증가")
    void testTickIncrement() throws InterruptedException {
        TimerNode timer = new TimerNode("t1", 50);
        Connection conn = new Connection("c1");
        timer.getOutputPort("out").connect(conn);

        timer.initialize();
        Thread.sleep(200);
        timer.shutdown();

        int firstTick = (int) conn.poll().get("tick");
        int secondTick = (int) conn.poll().get("tick");

        assertEquals(0, firstTick);
        assertEquals(1, secondTick);
    }

    @Test
    @DisplayName("3. shutdown 후 정지")
    void testShutdownStopsGeneration() throws InterruptedException {
        TimerNode timer = new TimerNode("t1", 100);
        Connection conn = new Connection("c1");
        timer.getOutputPort("out").connect(conn);

        timer.initialize();
        Thread.sleep(150);
        timer.shutdown();

        int countAfterShutdown = conn.getBufferSize();
        Thread.sleep(300);

        assertEquals(countAfterShutdown, conn.getBufferSize(), "shutdown 후에는 메시지가 추가로 생성되지 않아야 함");
    }

    @Test
    @DisplayName("4. 주기 확인")
    void testIntervalAccuracy() throws InterruptedException {
        TimerNode timer = new TimerNode("t1", 500);
        Connection conn = new Connection("c1");
        timer.getOutputPort("out").connect(conn);

        timer.initialize();
        Thread.sleep(2100);
        timer.shutdown();

        int size = conn.getBufferSize();
        assertTrue(size >= 4 && size <= 6, "500ms 주기 시 2.1초 동안 4~5개의 메시지가 생성되어야 함 (현재: " + size + ")");
    }
}