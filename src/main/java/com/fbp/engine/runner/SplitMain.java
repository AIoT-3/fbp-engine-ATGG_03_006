package com.fbp.engine.runner;

import com.fbp.engine.node.TimerNode;
import com.fbp.engine.node.SplitNode;
import com.fbp.engine.node.PrintNode;

public class SplitMain {
    public static void main(String[] args) {
        TimerNode timer = new TimerNode("timer", 3);

        SplitNode splitter = new SplitNode("splitter", "tick", 3.0);

        PrintNode warningPrinter = new PrintNode("경고");
        PrintNode normalPrinter = new PrintNode("정상");

        timer.connect("out", splitter, "in");

        splitter.connect("match", warningPrinter, "in");
        splitter.connect("mismatch", normalPrinter, "in");

        timer.start();
        splitter.start();
        warningPrinter.start();
        normalPrinter.start();
    }
}