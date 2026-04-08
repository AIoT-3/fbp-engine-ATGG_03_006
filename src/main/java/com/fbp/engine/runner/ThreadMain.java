package com.fbp.engine.runner;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import com.fbp.engine.node.FilterNode;
import com.fbp.engine.node.GeneratorNode;
import com.fbp.engine.node.PrintNode;

public class ThreadMain {
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        GeneratorNode generator = new GeneratorNode("gen-1");
        FilterNode filter = new FilterNode("filter-30", "temp", 30.0);
        PrintNode printer = new PrintNode("print-1");

        Connection conn1 = new Connection("conn-1", 10); // Gen -> Filter
        Connection conn2 = new Connection("conn-2", 10); // Filter -> Print

        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (running && i < 20) {
                // 25~35 사이의 온도를 무작위 생성하여 전달
                double temp = 25 + (Math.random() * 10);
                generator.generate("temp", temp);
                System.out.println("[Thread-1] 생산: " + String.format("%.1f", temp));

                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
                i++;
            }
            System.out.println("Thread-1 종료");
        });
        // 스레드 2 : FilterNode (중간 처리기)
        Thread thread2 = new Thread(() -> {
            while (running) {
                Message msg = conn1.poll();
                if (msg != null) {
                    filter.process(msg);
                }
            }
        });

        // 스레드 3: PrintNode (소비자)
        Thread thread3 = new Thread(() -> {
            while (running) {
                Message msg = conn2.poll();
                if (msg != null) {
                    printer.process(msg);
                }
            }
        });

        generator.getOutputPort().connect(conn1);
        filter.getOutputPort().connect(conn2);

        thread1.start();
        thread2.start();
        thread3.start();

        // 10초 후 종료 테스트
        Thread.sleep(10000);
        running = false;
        thread1.interrupt();
        thread2.interrupt();
        thread3.interrupt();
    }
}