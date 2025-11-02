package graph.topo;

import util.metrics.Metrics;

import java.util.List;

public class TopoResult {
    private final List<Integer> order;
    private final long timeNs;
    private final Metrics metrics;

    public TopoResult(List<Integer> order, long timeNs, Metrics metrics) {
        this.order = order;
        this.timeNs = timeNs;
        this.metrics = metrics;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public long getTimeNs() {
        return timeNs;
    }

    @Override
    public String toString() {
        return "Topo order: " + order + "\nTime: " + timeNs + " ns";
    }
    public Metrics getMetrics() {
        return metrics;
    }
}
