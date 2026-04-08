package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionBlockingTest {

    @Test
    @DisplayName("1. deliver-poll 기본 동작")
    void testDeliverPollBasic() {
        Connection conn = new Connection("c1");
        Message msg = new Message(Map.of("v", 1));

        conn.deliver(msg);
        Message polled = conn.poll();

        assertNotNull(polled);
        assertEquals(msg, polled);
    }

    @Test
    @DisplayName("2. 메시지 순서 보장")
    void testMessageOrder() {
        Connection conn = new Connection("c1");

        conn.deliver(new Message(Map.of("seq", 1)));
        conn.deliver(new Message(Map.of("seq", 2)));
        conn.deliver(new Message(Map.of("seq", 3)));

        assertEquals(1, (Integer) conn.poll().get("seq"));
        assertEquals(2, (Integer) conn.poll().get("seq"));
        assertEquals(3, (Integer) conn.poll().get("seq"));
    }

    @Test
    @DisplayName("3. 멀티스레드 deliver-poll")
    void testMultiThreadDeliverPoll() throws InterruptedException {
        Connection conn = new Connection("c1");
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Message> received = new AtomicReference<>();

        Thread consumer = new Thread(() -> {
            received.set(conn.poll());
            latch.countDown();
        });
        consumer.start();

        Message msg = new Message(Map.of("data", "async"));
        Thread.sleep(100);
        conn.deliver(msg);

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(msg, received.get());
    }

    @Test
    @DisplayName("4. poll 대기 동작")
    void testPollBlocking() throws InterruptedException {
        Connection conn = new Connection("c1");
        long startTime = System.currentTimeMillis();

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(500);
                conn.deliver(new Message(Map.of()));
            } catch (InterruptedException ignored) {}
        });
        producer.start();

        conn.poll();
        long duration = System.currentTimeMillis() - startTime;

        assertTrue(duration >= 500, "poll()은 메시지가 올 때까지 블로킹되어야 함");
    }

    @Test
    @DisplayName("5. 버퍼 크기 제한")
    void testDeliverBlockingOnFullBuffer() throws InterruptedException {
        Connection conn = new Connection("c1", 2);
        conn.deliver(new Message(Map.of("id", 1)));
        conn.deliver(new Message(Map.of("id", 2)));

        CountDownLatch latch = new CountDownLatch(1);
        Thread producer = new Thread(() -> {
            conn.deliver(new Message(Map.of("id", 3)));
            latch.countDown();
        });
        producer.start();

        assertFalse(latch.await(500, TimeUnit.MILLISECONDS), "버퍼가 가득 차면 deliver()는 대기해야 함");

        conn.poll();
        assertTrue(latch.await(500, TimeUnit.MILLISECONDS), "공간이 생기면 deliver()가 완료되어야 함");
    }

    @Test
    @DisplayName("6. 버퍼 크기 조회")
    void testGetBufferSize() {
        Connection conn = new Connection("c1", 10);
        conn.deliver(new Message(Map.of()));
        conn.deliver(new Message(Map.of()));

        assertEquals(2, conn.getBufferSize());

        conn.poll();
        assertEquals(1, conn.getBufferSize());
    }
}
