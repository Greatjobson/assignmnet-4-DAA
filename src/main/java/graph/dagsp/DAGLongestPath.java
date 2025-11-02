package graph.dagsp;

import graph.model.Graph;
import graph.topo.TopoSort;
import graph.topo.TopoResult;
import util.metrics.Metrics;

import java.util.List;

/**
 * DAG Longest Path algorithm (works only on DAGs).
 * Tracks relaxations and total time.
 */
public class DAGLongestPath {

    /**
     * Computes longest paths from a single source in a DAG.
     * @param dag directed acyclic graph
     * @param src source vertex
     * @return longest path distances and predecessors
     */
    public static PathResult computeLongest(Graph dag, int src) {
        Metrics metrics = new Metrics();
        metrics.startTimer();

        TopoResult topoResult = TopoSort.kahn(dag);
        List<Integer> topoOrder = topoResult.getOrder();

        int n = dag.size();
        int[] dist = new int[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MIN_VALUE;
            prev[i] = -1;
        }
        dist[src] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (var e : dag.getEdges(u)) {
                    metrics.incrementCounter("Edges processed");
                    if (dist[e.v] < dist[u] + e.w) {
                        dist[e.v] = dist[u] + e.w;
                        prev[e.v] = u;
                        metrics.incrementCounter("Relaxations");
                    }
                }
            }
        }

        metrics.stopTimer();
        long time = metrics.getExecutionTime();

        return new PathResult(dist, prev, metrics, time);
    }
}
