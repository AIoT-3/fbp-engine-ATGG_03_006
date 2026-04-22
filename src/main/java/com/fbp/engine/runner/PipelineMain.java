package com.fbp.engine.runner;

import com.fbp.engine.node.TimerNode;
import com.fbp.engine.node.LogNode;
import com.fbp.engine.node.FilterNode;
import com.fbp.engine.node.PrintNode;
import com.fbp.engine.core.AbstractNode;
import java.util.List;

public class PipelineMain {
    public static void main(String[] args) {
        TimerNode timer = new TimerNode("timer", 3);
        LogNode logger = new LogNode("logger");

        FilterNode filter = new FilterNode("filter", "tick", 3.0);

        PrintNode printer = new PrintNode("printer");

        List<AbstractNode> nodes = List.of(timer, logger, filter, printer);

        timer.connect("out", logger, "in");
        logger.connect("out", filter, "in");
        filter.connect("out", printer, "in");

        System.out.println("=== 파이프라인 초기화 및 시작 ===");
        for (AbstractNode node : nodes) {
            node.initialize();
            node.start();
        }

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("=== 7초 경과: 파이프라인 종료 ===");
        for (AbstractNode node : nodes) {
            node.shutdown();
        }
    }
}