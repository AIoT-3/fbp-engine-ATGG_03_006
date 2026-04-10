package com.fbp.engine.runner;

import com.fbp.engine.core.Connection;
import com.fbp.engine.node.FilterNode;
import com.fbp.engine.node.PrintNode;
import com.fbp.engine.node.TimerNode;

public class TimerMain {
    public static void main(String[] args) throws InterruptedException {
        TimerNode timer = new TimerNode("timer", 500);
        FilterNode filter = new FilterNode("filter", "tick", 3.0);
        PrintNode printer = new PrintNode("printer");

        Connection conn1 = new Connection("c1");
        Connection conn2 = new Connection("c2");

        timer.getOutputPort("out").connect(conn1);
        filter.getInputPort("in").connect(conn1);

        filter.getOutputPort("out").connect(conn2);
        printer.getInputPort("in").connect(conn2);

        timer.initialize();
        filter.initialize();
        printer.initialize();

        System.out.println("--- 파이프라인 시작 (3초간 실행) ---");

        Thread.sleep(3000);

        System.out.println("--- 파이프라인 종료 중 ---");
        timer.shutdown();
        filter.shutdown();
        printer.shutdown();
    }
}
