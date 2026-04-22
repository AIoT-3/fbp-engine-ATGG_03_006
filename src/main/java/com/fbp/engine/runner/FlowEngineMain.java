package com.fbp.engine.runner;

import com.fbp.engine.core.Flow;
import com.fbp.engine.core.FlowEngine;
import com.fbp.engine.node.PrintNode;
import com.fbp.engine.node.TimerNode;

public class FlowEngineMain {
    public static void main(String[] args) throws InterruptedException {
        FlowEngine engine = new FlowEngine();

        Flow monitoringFlow = new Flow("monitoring")
                .addNode(new TimerNode("timer", 1000))
                .addNode(new PrintNode("printer"))
                .connect("timer", "out", "printer", "in");

        engine.register(monitoringFlow);
        engine.startFlow("monitoring");

        Thread.sleep(5000);
        engine.stopFlow("monitoring");

        System.out.println("\n--- 과제 8-3: 다중 플로우 실행 시작 ---\n");

        Flow flowA = new Flow("Flow-A")
                .addNode(new TimerNode("timerA", 500))
                .addNode(new PrintNode("A"))
                .connect("timerA", "out", "A", "in");

        Flow flowB = new Flow("Flow-B")
                .addNode(new TimerNode("timerB", 1000))
                .addNode(new PrintNode("B"))
                .connect("timerB", "out", "B", "in");

        engine.register(flowA);
        engine.register(flowB);

        engine.startFlow("Flow-A");
        engine.startFlow("Flow-B");

        Thread.sleep(5000);

        engine.shutdown();
    }
}
