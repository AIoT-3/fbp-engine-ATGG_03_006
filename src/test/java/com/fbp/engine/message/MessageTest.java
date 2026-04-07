package com.fbp.engine.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    @DisplayName("1. 생성 시 ID 자동 할당")
    void testIdAssignment() {
        Message message = new Message(new HashMap<>());
        assertNotNull(message.getId());
    }

    @Test
    @DisplayName("2. 생성 시 timestamp 자동 기록")
    void testTimestamp() {
        Message message = new Message(new HashMap<>());
        assertTrue(message.getTimestamp().toEpochMilli() > 0);
    }

    @Test
    @DisplayName("3. 페이로드 조회")
    void testGetPayloadValue() {
        Map<String, Object> data = Map.of("key", "value");
        Message message = new Message(data);
        assertEquals("value", message.get("key"));
    }

    @Test
    @DisplayName("4. 제네릭 get 타입 캐스팅")
    void testGenericGet() {
        Message message = new Message(Map.of("temperature", 25.5));
        Double temp = message.get("temperature"); // 캐스팅 확인
        assertEquals(25.5, temp);
    }

    @Test
    @DisplayName("5. 존재하지 않는 키 조회")
    void testGetMissingKey() {
        Message message = new Message(new HashMap<>());
        assertNull(message.get("없는키"));
    }

    @Test
    @DisplayName("6. 페이로드 불변 — 외부 수정 차단")
    void testPayloadUnmodifiable() {
        Message message = new Message(Map.of("a", 1));
        assertThrows(UnsupportedOperationException.class, () -> {
            message.getPayload().put("b", 2);
        });
    }

    @Test
    @DisplayName("7. 페이로드 불변 — 원본 Map 수정 무영향")
    void testOriginalMapIsolation() {
        Map<String, Object> original = new HashMap<>();
        original.put("a", 1);
        Message message = new Message(original);

        original.put("a", 99); // 원본 수정
        assertEquals(1, (Integer) message.get("a")); // 메시지 내용은 유지되어야 함
    }

    @Test
    @DisplayName("8. withEntry — 새 객체 반환")
    void testWithEntryReturnsNewObject() {
        Message original = new Message(Map.of("a", 1));
        Message next = original.withEntry("b", 2);
        assertNotSame(original, next);
    }

    @Test
    @DisplayName("9. withEntry — 원본 불변")
    void testWithEntryOriginalImmutability() {
        Message original = new Message(Map.of("a", 1));
        original.withEntry("b", 2);
        assertFalse(original.hasKey("b"));
    }

    @Test
    @DisplayName("10. withEntry — 새 메시지에 값 존재")
    void testWithEntryContainsNewValue() {
        Message message = new Message(Map.of("a", 1)).withEntry("b", 2);
        assertEquals(2, (Integer) message.get("b"));
    }

    @Test
    @DisplayName("11. hasKey — 존재하는 키")
    void testHasKeyTrue() {
        Message message = new Message(Map.of("temperature", 20.0));
        assertTrue(message.hasKey("temperature"));
    }

    @Test
    @DisplayName("12. hasKey — 없는 키")
    void testHasKeyFalse() {
        Message message = new Message(new HashMap<>());
        assertFalse(message.hasKey("없는키"));
    }

    @Test
    @DisplayName("13. withoutKey — 키 제거 확인")
    void testWithoutKeyRemoval() {
        Message original = new Message(Map.of("a", 1, "b", 2));
        Message next = original.withoutKey("a");
        assertFalse(next.hasKey("a"));
        assertTrue(next.hasKey("b"));
    }

    @Test
    @DisplayName("14. withoutKey — 원본 불변")
    void testWithoutKeyOriginalImmutability() {
        Message original = new Message(Map.of("a", 1));
        original.withoutKey("a");
        assertTrue(original.hasKey("a"));
    }

    @Test
    @DisplayName("15. toString 포맷")
    void testToStringFormat() {
        Message message = new Message(Map.of("key", "val"));
        String str = message.toString();
        assertNotNull(str);
        assertTrue(str.contains("key") && str.contains("val"));
    }
}