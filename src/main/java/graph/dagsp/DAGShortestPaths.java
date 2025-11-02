package graph.dagsp;

import graph.model.Graph;
import graph.topo.TopoSort;
import graph.topo.TopoResult;
import util.metrics.Metrics;
import java.util.List;

/**
 * DAG Shortest Path algorithm (works only on DAGs).
 * Tracks relaxations and total time.
 */
public class DAGShortestPaths {

    /**
     * Computes shortest paths from a single source in a DAG.
     * @param dag directed acyclic graph
     * @param src source vertex
     * @return shortest path distances and predecessors
     */
    public static PathResult computeShortest(Graph dag, int src) {
        Metrics metrics = new Metrics();
        metrics.startTimer();

        // Topological order
        TopoResult topoResult = TopoSort.kahn(dag);
        List<Integer> topoOrder = topoResult.getOrder();

        int n = dag.size();
        int[] dist = new int[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }
        dist[src] = 0;

        // Relax edges in topological order
        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (var e : dag.getEdges(u)) {
                    metrics.incrementCounter("Edges processed");
                    if (dist[e.v] > dist[u] + e.w) {
                        dist[e.v] = dist[u] + e.w;
                        prev[e.v] = u;
                        metrics.incrementCounter("Relaxations");
                    }
                }
            }
        }

        metrics.stopTimer();
        long time = metrics.getExecutionTime();

        // Можно вывести метрики вручную позже
        // metrics.printMetrics();

        return new PathResult(dist, prev, metrics, time);
    }
}
