package com.fbp.engine.node;

import com.fbp.engine.message.Message;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerNode extends AbstractNode{
    private final long intervalMs;
    private int tickCount = 0;
    private ScheduledExecutorService scheduler;

    public TimerNode(String id, long intervalMs) {
        super(id);
        this.intervalMs = intervalMs;
        addOutputPort("out");
    }

    @Override
    public void initialize() {
        super.initialize();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        this.scheduler.scheduleAtFixedRate(() -> {
            Message message = new Message(Map.of(
                    "tick", tickCount++,
                    "timestamp", System.currentTimeMillis()
            ));
            send("out", message);
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()){
            scheduler.shutdown();
        }
        super.shutdown();
    }

    @Override
    protected void onProcess(Message message) {

    }
}
