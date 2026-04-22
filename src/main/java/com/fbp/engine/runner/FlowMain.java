package com.fbp.engine.runner;

import com.fbp.engine.core.Flow;
import com.fbp.engine.node.*;

public class FlowMain {
    public static void main(String[] args) {
        Flow tempFlow = new Flow("Temp-Pipeline")
                .addNode(new TimerNode("timer", 1000))
                .addNode(new LogNode("logger"))
                .addNode(new TransformNode("f2c", msg -> { /* F to C 로직 */ return msg; }))
                .addNode(new PrintNode("printer"))
                .connect("timer", "out", "logger", "in")
                .connect("logger", "out", "f2c", "in")
                .connect("f2c", "out", "printer", "in");

        Flow splitFlow = new Flow("Split-Pipeline")
                .addNode(new TimerNode("gen", 500))
                .addNode(new FilterNode("filter-high", "val", 50))
                .addNode(new PrintNode("print-a"))
                .addNode(new PrintNode("print-b"))
                .connect("gen", "out", "filter-high", "in")
                .connect("filter-high", "out", "print-a", "in") // 조건 만족 시 A
                .connect("filter-high", "out", "print-b", "in"); // 동일 메시지 B로도 전송 (멀티캐스트)

        tempFlow.initialize();
        tempFlow.shutdown();
    }
}