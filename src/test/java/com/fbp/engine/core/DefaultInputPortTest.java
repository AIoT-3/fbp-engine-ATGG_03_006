package com.fbp.engine.core;

import com.fbp.engine.message.Message;
import com.fbp.engine.node.PrintNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultInputPortTest {

    @Test
    @DisplayName("1. receive 시 owner 호출")
    void testReceiveCallsOwner() {
        // 소속 노드의 process() 호출 여부를 확인할 변수
        final boolean[] isCalled = {false};

        // 익명 클래스로 테스트용 Node 생성
        Node owner = new Node() {
            @Override
            public String getId() { return "test-node"; }

            @Override
            public void process(Message message) {
                isCalled[0] = true;
            }
        };

        DefaultInputPort inputPort = new DefaultInputPort("in-1", owner);
        inputPort.receive(new Message(Map.of()));

        // owner.process()가 호출되었는지 검증
        assertTrue(isCalled[0]);
    }

    @Test
    @DisplayName("2. 포트 이름 확인")
    void testPortName() {
        String expectedName = "in-port";
        Node owner = new PrintNode("p1"); // 기존에 만든 PrintNode 활용

        DefaultInputPort inputPort = new DefaultInputPort(expectedName, owner);

        // getName()이 생성 시 지정한 이름을 반환하는지 확인
        assertEquals(expectedName, inputPort.getName());
    }
}
