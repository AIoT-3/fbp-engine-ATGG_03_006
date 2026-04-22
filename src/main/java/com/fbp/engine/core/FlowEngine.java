package com.fbp.engine.core;

import java.util.*;

public class FlowEngine {
    private final Map<String, Flow> flows = new HashMap<>();

    public void register(Flow flow) {
        flows.put(flow.getId(), flow);
    }

    public void startFlow(String flowId) {
        Flow flow = flows.get(flowId);
        if (flow != null) {
            flow.initialize();
            System.out.println("[Engine] 플로우 '" + flowId + "' 시작됨");
        }
    }

    public void stopFlow(String flowId) {
        Flow flow = flows.get(flowId);
        if (flow != null) {
            flow.shutdown();
            System.out.println("[Engine] 플로우 '" + flowId + "' 정지됨");
        }
    }

    public void listFlows() {
        int i = 1;
        for (Flow flow : flows.values()) {
            System.out.printf("[%d] %-15s %s%n", i++, flow.getId(), flow.getState());
        }
    }

    public void runCLI() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("fbp> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            String command = parts[0];

            if (command.equals("exit")) {
                flows.values().forEach(Flow::shutdown);
                System.out.println("[Engine] 엔진 종료됨");
                break;
            }

            switch (command) {
                case "list":
                    listFlows();
                    break;
                case "start":
                    if (parts.length > 1) startFlow(parts[1]);
                    break;
                case "stop":
                    if (parts.length > 1) stopFlow(parts[1]);
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
    public void shutdown() {
        for (Flow flow : flows.values()) {
            flow.shutdown();
        }
        System.out.println("[Engine] 모든 플로우가 정지되고 엔진이 종료되었습니다.");
    }
}