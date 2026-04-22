package com.fbp.engine.runner;

import com.fbp.engine.node.*;
import com.fbp.engine.message.Message;
import java.util.Map;

public class TransformMain {
    public static void main(String[] args) {
        GeneratorNode generator = new GeneratorNode("generator");

        TransformNode fahrenheitToCelsius = new TransformNode("converter", (message) -> {
            try {
                Map<String, Object> payload = (Map<String, Object>) message.getPayload();
                double f = (double) payload.get("value");
                double c = (f - 32) * 5 / 9;
                return new Message(Map.of("value", c));
            } catch (Exception e) {
                return null;
            }
        });

        PrintNode printer = new PrintNode("printer");

        generator.connect("out", fahrenheitToCelsius, "in");
        fahrenheitToCelsius.connect("out", printer, "in");

        generator.start();
        fahrenheitToCelsius.start();
        printer.start();

        generator.generate("value", 100.0);
    }
}