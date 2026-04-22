package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
    private final String id;
    private BlockingQueue<Message> buffer;
    private InputPort target;

    public Connection(String id) {
        this(id, 100);
    }

    public Connection(String id, int capacity) {
        this.id = id;
        this.buffer = new LinkedBlockingQueue<>(capacity);
    }

    public String getId() {
        return id;
    }

    public void deliver(Message message) {
        buffer.add(message);
        if (this.target != null) {
            this.target.receive(message);
        }
    }

    public Message poll() {
        try {
            return buffer.poll(); // take() 대신 poll()을 쓰거나 비어있는지 확인 필요
        } catch (Exception e) {
            return null;
        }
    }

    public void setTarget(InputPort target) {
        this.target = target;
        // [추가] 만약 타겟이 설정될 때 버퍼에 대기 중인 메시지가 있다면 전달할 수도 있음
    }

    public int getBufferSize() {
        return buffer.size();
    }
}