package com.fbp.engine.node;

import com.fbp.engine.core.AbstractNode;
import com.fbp.engine.message.Message;

public class AlertNode extends AbstractNode {

    public AlertNode(String id) {
        super(id);
        addInputPort("in");
    }

    @Override
    protected void onProcess(Message message) {
        if (message.hasKey("sensorId") && message.hasKey("temperature")) {
            String sensorId = message.get("sensorId");
            Object temperature = message.get("temperature");

            System.out.println("[경고] 센서 " + sensorId + " 온도 " + temperature + "°C — 임계값 초과!");
        } else {
            System.out.println("[경고] 알 수 없는 센서 데이터");
        }
    }

    @Override
    public void deliver(Message m) {
        onProcess(m);
    }
}