package com.fbp.engine.runner;

import com.fbp.engine.message.Message;
import com.fbp.engine.node.PrintNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("temperature", 25.5);

        Message message = new Message(data);

        PrintNode printer = new PrintNode("printer-1");

        System.out.println("Test");
        printer.process(message);
    }
}
