package com.fbp.engine.runner;

import com.fbp.engine.core.Flow;
import com.fbp.engine.core.FlowEngine;
import com.fbp.engine.node.PrintNode;
import com.fbp.engine.node.TimerNode;

public class FlowCliMain {
    public static void main(String[] args) {
        FlowEngine engine = new FlowEngine();

        Flow monitoring = new Flow("monitoring")
                .addNode(new TimerNode("t1", 1000))
                .addNode(new PrintNode("p1"))
                .connect("t1", "out", "p1", "in");

        engine.register(monitoring);
        engine.runCLI();
    }
}
