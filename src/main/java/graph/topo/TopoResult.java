package graph.topo;

import java.util.List;

public class TopoResult {
    private final List<Integer> order;
    private final long timeNs;

    public TopoResult(List<Integer> order, long timeNs) {
        this.order = order;
        this.timeNs = timeNs;
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
}
