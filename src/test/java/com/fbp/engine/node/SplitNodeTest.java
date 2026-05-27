package com.fbp.engine.node;

import static org.junit.jupiter.api.Assertions.*;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SplitNodeTest {
    private SplitNode node;
    private List<Message> matchResults;
    private List<Message> mismatchResults;

    @BeforeEach
    void setUp() {
        node = new SplitNode("s1", "value", 5.0);
        matchResults = new ArrayList<>();
        mismatchResults = new ArrayList<>();

        AbstractNode matchTarget = new AbstractNode("m1") {
            {
                addInputPort("in");
            }

            @Override
            protected void onProcess(Message m) {
                matchResults.add(m);
            }
            @Override
            public void deliver(Message m) {
                onProcess(m);
            }
        };

        AbstractNode mismatchTarget = new AbstractNode("m2") {
            {
                addInputPort("in");
            }

            @Override
            protected void onProcess(Message m) {
                mismatchResults.add(m);
            }
            @Override
            public void deliver(Message m) {
                onProcess(m);
            }
        };

        node.connect("match", matchTarget, "in");
        node.connect("mismatch", mismatchTarget, "in");
    }

    @Test
    @DisplayName("조건 만족 → match 포트")
    void testThresholdMatch() {
        node.process(new Message(Map.of("value", 10.0)));
        assertEquals(1, matchResults.size());
    }

    @Test
    @DisplayName("조건 미달 → mismatch 포트")
    void testThresholdMismatch() {
        node.process(new Message(Map.of("value", 2.0)));
        assertEquals(1, mismatchResults.size());
    }

    @Test
    @DisplayName("양쪽 동시 확인")
    void testSimultaneousSplit() {
        node.process(new Message(Map.of("value", 10.0)));
        node.process(new Message(Map.of("value", 2.0)));
        assertEquals(1, matchResults.size());
        assertEquals(1, mismatchResults.size());
    }

    @Test
    @DisplayName("경계값 처리")
    void testBoundaryValue() {
        node.process(new Message(Map.of("value", 5.0)));
        assertEquals(1, matchResults.size());
    }
}