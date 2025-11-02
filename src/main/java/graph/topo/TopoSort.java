package graph.topo;

import graph.model.Graph;
import util.metrics.*;
import util.metrics.MetricsInterface;

import java.util.*;

/**
 * Kahn's algorithm for Topological Sorting.
 * Tracks metrics: queue operations (push/pop) and edge relaxations.
 */
public class TopoSort {

    /**
     * Performs topological sorting using Kahn's algorithm.
     * @param dag input directed acyclic graph
     * @return topological order and execution time
     */
    public static TopoResult kahn(Graph dag) {
        Metrics metrics = new Metrics();
        metrics.startTimer();

        int n = dag.size();
        int[] indeg = new int[n];

        // Count indegrees
        for (int u = 0; u < n; u++) {
            for (var e : dag.getEdges(u)) {
                indeg[e.v]++;
                metrics.incrementCounter("Edges processed");
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
                metrics.incrementCounter("Pushes");
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.incrementCounter("Pops");
            order.add(u);

            for (var e : dag.getEdges(u)) {
                indeg[e.v]--;
                metrics.incrementCounter("Relaxations");
                if (indeg[e.v] == 0) {
                    q.add(e.v);
                    metrics.incrementCounter("Pushes");
                }
            }
        }

        metrics.stopTimer();
        long time = metrics.getExecutionTime();

        if (order.size() != n)
            throw new IllegalStateException("Graph is not a DAG!");




        return new TopoResult(order, time, metrics);
    }
}
