package util.metrics;

/**
 * Metrics interface for tracking algorithm performance.
 * Provides operation counters and timing methods.
 */
public interface MetricsInterface {
    /** Start measuring execution time. */
    void startTimer();

    /** Stop measuring execution time. */
    void stopTimer();

    /** @return total execution time in nanoseconds */
    long getExecutionTime();

    /**
     * Increment a specific operation counter by 1.
     * @param operationName name of the operation
     */
    void incrementCounter(String operationName);

    /**
     * Add a value to a counter.
     * @param operationName name of the operation
     * @param value amount to add
     */
    void addToCounter(String operationName, long value);

    /**
     * @param operationName name of the operation
     * @return the current counter value
     */
    long getCounter(String operationName);

    /** Print all collected metrics. */
    void printMetrics();
}
