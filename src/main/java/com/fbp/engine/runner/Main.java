package com.fbp.engine.runner;

import com.fbp.engine.core.Connection;
import com.fbp.engine.message.Message;
import com.fbp.engine.node.FilterNode;
import com.fbp.engine.node.GeneratorNode;
import com.fbp.engine.node.PrintNode;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        System.out.println("=== 과제 3-7 테스트 ===");
        GeneratorNode generator = new GeneratorNode("gen-1");
        PrintNode printer = new PrintNode("print-1");

        Connection conn1 = new Connection("conn-1");
        conn1.setTarget(printer.getInputPort());
        generator.getOutputPort().connect(conn1);

        generator.generate("temperature", 25.5);

        System.out.println("\n=== 과제 3-8 테스트 ===");
        PrintNode printer2 = new PrintNode("print-2");
        Connection conn2 = new Connection("conn-2");
        conn2.setTarget(printer2.getInputPort());

        generator.getOutputPort().connect(conn2);
        generator.generate("humidity", 60.0);

        System.out.println("\n=== 과제 3-10 테스트 ===");
        GeneratorNode gen3 = new GeneratorNode("gen-3");
        FilterNode filter = new FilterNode("filter-30", "temperature", 30.0);
        PrintNode printer3 = new PrintNode("print-3");

        Connection c1 = new Connection("c1");
        c1.setTarget(filter.getInputPort());
        gen3.getOutputPort().connect(c1);

        Connection c2 = new Connection("c2");
        c2.setTarget(printer3.getInputPort());
        filter.getOutputPort().connect(c2);

        System.out.println("테스트 1: 온도 25.0 (출력 X)");
        gen3.generate("temperature", 25.0);

        System.out.println("테스트 2: 온도 35.0 (출력 O)");
        gen3.generate("temperature", 35.0);
    }
}
