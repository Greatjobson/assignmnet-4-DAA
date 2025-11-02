package graph.dagsp;

import util.metrics.Metrics;
import java.util.List;
import java.util.ArrayList;

/**
 * Stores distance and predecessor arrays for path algorithms.
 * Optionally includes performance metrics.
 */
public class PathResult {
    private final int[] dist;
    private final int[] prev;
    private final Metrics metrics;
    private final long timeNs;

    public PathResult(int[] dist, int[] prev, Metrics metrics, long timeNs) {
        this.dist = dist;
        this.prev = prev;
        this.metrics = metrics;
        this.timeNs = timeNs;
    }

    public int[] getDist() {
        return dist;
    }

    public int[] getPrev() {
        return prev;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public long getTimeNs() {
        return timeNs;
    }

    /**
     * Reconstructs path from source to target.
     * @param target destination vertex
     * @return list of vertices representing the path
     */
    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(0, at);
        }
        return path;
    }

    @Override
    public String toString() {
        return "Path distances: " + java.util.Arrays.toString(dist)
                + "\nTime: " + timeNs + " ns";
    }
}
