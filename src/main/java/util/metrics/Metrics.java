package util.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the Metrics interface.
 * Tracks counters for operations and measures execution time.
 */
public class Metrics implements MetricsInterface {
    private final Map<String, Long> counters = new HashMap<>();
    private long startTime;
    private long totalTime;

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        totalTime = System.nanoTime() - startTime;
    }

    @Override
    public long getExecutionTime() {
        return totalTime;
    }

    @Override
    public void incrementCounter(String operationName) {
        addToCounter(operationName, 1);
    }

    @Override
    public void addToCounter(String operationName, long value) {
        counters.put(operationName, counters.getOrDefault(operationName, 0L) + value);
    }

    @Override
    public long getCounter(String operationName) {
        return counters.getOrDefault(operationName, 0L);
    }

    @Override
    public void printMetrics() {
        System.out.println("=== Metrics Report ===");
        System.out.printf("Execution time: %.3f ms%n", totalTime / 1_000_000.0);
        for (var entry : counters.entrySet()) {
            System.out.printf("%s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("======================");
    }
}
