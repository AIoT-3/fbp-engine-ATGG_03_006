package com.fbp.engine.node;

import com.fbp.engine.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterNodeTest {
    private static final String TEST_FILE_PATH = "test_output.log";
    private FileWriterNode fileWriterNode;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void tearDown() {
        if (fileWriterNode != null) {
            fileWriterNode.shutdown();
        }
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("파일 생성 검증: initialize() 후 지정 경로에 파일이 생성된다.")
    void testFileCreation() {
        fileWriterNode = new FileWriterNode("writer1", TEST_FILE_PATH);
        fileWriterNode.initialize();

        File file = new File(TEST_FILE_PATH);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("내용 기록 검증: 메시지 3개를 보낸 후 shutdown하면 파일에 3줄이 기록된다.")
    void testContentRecording() throws IOException {
        fileWriterNode = new FileWriterNode("writer2", TEST_FILE_PATH);
        fileWriterNode.initialize();

        Message msg1 = new Message(Map.of("data", "First Line"));
        Message msg2 = new Message(Map.of("data", "Second Line"));
        Message msg3 = new Message(Map.of("data", "Third Line"));

        fileWriterNode.onProcess(msg1);
        fileWriterNode.onProcess(msg2);
        fileWriterNode.onProcess(msg3);

        fileWriterNode.shutdown();

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE_PATH));
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("First Line"));
        assertTrue(lines.get(1).contains("Second Line"));
        assertTrue(lines.get(2).contains("Third Line"));
    }

    @Test
    @DisplayName("파일 닫힘 검증: shutdown() 후 추가 메시지를 보내도 파일에 기록되지 않는다.")
    void testPostShutdownNoRecording() {
        fileWriterNode = new FileWriterNode("writer3", TEST_FILE_PATH);
        fileWriterNode.initialize();
        fileWriterNode.shutdown();

        Message postMsg = new Message(Map.of("data", "Post Shutdown Line"));

        assertDoesNotThrow(() -> {
            fileWriterNode.onProcess(postMsg);
        });

        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(Path.of(TEST_FILE_PATH));
                assertTrue(lines.isEmpty() || lines.stream().noneMatch(line -> line.contains("Post Shutdown Line")));
            } catch (IOException e) {
                fail();
            }
        }
    }
}