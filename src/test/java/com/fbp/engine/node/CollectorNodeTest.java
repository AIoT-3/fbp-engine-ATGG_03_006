package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CollectorNodeTest {

    @Test
    @DisplayName("초기 상태 빈 리스트: 생성 직후 getCollected()가 빈 리스트를 반환한다.")
    void testInitialStateIsEmpty() {
        CollectorNode collector = new CollectorNode("collector");

        assertNotNull(collector.getCollected());
        assertTrue(collector.getCollected().isEmpty());
    }

    @Test
    @DisplayName("InputPort 존재: 'in' 포트가 정상적으로 등록되어 있다.")
    void testInputPortExists() {
        CollectorNode collector = new CollectorNode("collector");

        assertNotNull(collector.getInputPort("in"));
    }

    @Test
    @DisplayName("메시지 수집: 메시지를 전송하면 getCollected() 리스트에 저장된다.")
    void testMessageCollection() {
        CollectorNode collector = new CollectorNode("collector");
        Message msg = new Message(Map.of("value", 100));

        collector.onProcess(msg);

        assertEquals(1, collector.getCollected().size());
        assertEquals(msg, collector.getCollected().get(0));
    }

    @Test
    @DisplayName("수집 순서 보존: 여러 메시지를 순서대로 전송하면 리스트에 전송 순서대로 저장된다.")
    void testCollectionOrderPreserved() {
        CollectorNode collector = new CollectorNode("collector");
        Message first = new Message(Map.of("seq", 1));
        Message second = new Message(Map.of("seq", 2));
        Message third = new Message(Map.of("seq", 3));

        collector.onProcess(first);
        collector.onProcess(second);
        collector.onProcess(third);

        assertEquals(3, collector.getCollected().size());
        assertEquals(first, collector.getCollected().get(0));
        assertEquals(second, collector.getCollected().get(1));
        assertEquals(third, collector.getCollected().get(2));
    }

    @Test
    @DisplayName("파이프라인 연결 검증: GeneratorNode → CollectorNode 연결 시, Generator가 보낸 모든 메시지가 Collector에 수집된다.")
    void testPipelineIntegration() {
        MockGeneratorNode generator = new MockGeneratorNode("generator", 5);
        CollectorNode collector = new CollectorNode("collector");

        generator.setTargetNode(collector);

        generator.initialize();
        collector.initialize();

        generator.run();

        assertEquals(5, collector.getCollected().size());

        generator.shutdown();
        collector.shutdown();
    }

    private static class MockGeneratorNode extends AbstractNode {
        private final int messageCount;
        private CollectorNode targetNode;

        public MockGeneratorNode(String id, int messageCount) {
            super(id);
            this.messageCount = messageCount;
            addOutputPort("out");
        }

        public void setTargetNode(CollectorNode targetNode) {
            this.targetNode = targetNode;
        }

        public void run() {
            if (targetNode == null) {
                return;
            }
            for (int i = 0; i < messageCount; i++) {
                Message msg = new Message(Map.of("index", i));
                targetNode.onProcess(msg);
            }
        }

        @Override
        protected void onProcess(Message message) {

        }

        @Override
        public void deliver(Message m) {

        }
    }
}