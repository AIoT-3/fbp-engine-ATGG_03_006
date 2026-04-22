package com.fbp.engine.runner;

import com.fbp.engine.node.*;

public class DelayMain {
    public static void main(String[] args) {
        TimerNode timer = new TimerNode("timer", 100);
        DelayNode delay = new DelayNode("delay", 1000);
        CounterNode counter = new CounterNode("counter");
        PrintNode printer = new PrintNode("printer");

        timer.connect("out", delay, "in");
        delay.connect("out", counter, "in");
        counter.connect("out", printer, "in");

        timer.start();
        delay.start();
        counter.start();
        printer.start();
    }
}