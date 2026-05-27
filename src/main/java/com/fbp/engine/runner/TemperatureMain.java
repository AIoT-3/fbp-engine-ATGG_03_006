package com.fbp.engine.runner;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.node.*;
import com.fbp.engine.message.Message;

public class TemperatureMain {
    public static void main(String[] args) {
        TimerNode timer = new TimerNode("timer", 1000);

        TemperatureSensorNode tempSensor = new TemperatureSensorNode("temp-sensor", 15.0, 45.0);
        ThresholdFilterNode tempFilter = new ThresholdFilterNode("temp-filter", "temperature", 30.0);
        AlertNode tempAlert = new AlertNode("temp-alert");

        HumiditySensorNode humiSensor = new HumiditySensorNode("humi-sensor", 30.0, 90.0);
        ThresholdFilterNode humiFilter = new ThresholdFilterNode("humi-filter", "humidity", 70.0);

        AbstractNode humiAlert = new AbstractNode("humi-alert") {
            @Override
            protected void onProcess(Message message) {
                System.out.println("[습도경보] 센서 " + message.get("sensorId") +
                        " 습도 " + message.get("humidity") + "% — 임계값 초과!");
            }
            @Override
            public void deliver(Message m) { onProcess(m); }
        };

        AbstractNode logNode = new AbstractNode("log-node") {
            @Override
            protected void onProcess(Message message) {
                System.out.println("[정상] " + message.getPayload());
            }
            @Override
            public void deliver(Message m) { onProcess(m); }
        };

        timer.connect("out", tempSensor, "trigger");
        timer.connect("out", humiSensor, "trigger");

        tempSensor.connect("out", tempFilter, "in");
        tempFilter.connect("alert", tempAlert, "in");
        tempFilter.connect("normal", logNode, "in");

        humiSensor.connect("out", humiFilter, "in");
        humiFilter.connect("alert", humiAlert, "in");
        humiFilter.connect("normal", logNode, "in");

        timer.start();
        tempSensor.start();
        tempFilter.start();
        tempAlert.start();
        humiSensor.start();
        humiFilter.start();
        humiAlert.start();
        logNode.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        timer.shutdown();
        System.out.println("통합 모니터링 종료");
    }
}