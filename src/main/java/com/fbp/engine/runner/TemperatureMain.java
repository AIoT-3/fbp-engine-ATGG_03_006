package com.fbp.engine.runner;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.node.AlertNode;
import com.fbp.engine.node.TemperatureSensorNode;
import com.fbp.engine.node.ThresholdFilterNode;
import com.fbp.engine.message.Message;
import com.fbp.engine.node.TimerNode;

public class TemperatureMain {
    public static void main(String[] args) {
        TimerNode timer = new TimerNode("timer", 1000);
        TemperatureSensorNode sensor = new TemperatureSensorNode("sensor", 15.0, 45.0);
        ThresholdFilterNode filter = new ThresholdFilterNode("filter", "temperature", 30.0);
        AlertNode alert = new AlertNode("alert");

        AbstractNode log = new AbstractNode("log") {
            @Override
            protected void onProcess(Message message) {
                System.out.println("[정상] " + message.getPayload());
            }
            @Override
            public void deliver(Message m) { onProcess(m); }
        };

        timer.connect("out", sensor, "trigger");
        sensor.connect("out", filter, "in");
        filter.connect("alert", alert, "in");
        filter.connect("normal", log, "in");

        timer.start();
        sensor.start();
        filter.start();
        alert.start();
        log.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        timer.shutdown();
        sensor.shutdown();
        filter.shutdown();
        alert.shutdown();
        log.shutdown();

        System.out.println("모니터링이 종료되었습니다.");
    }
}