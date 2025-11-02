package graph.dagsp;

import graph.model.Graph;
import graph.topo.TopoSort;
import java.util.List;

public class DAGShortestPaths {

    public static PathResult computeShortest(Graph dag, int src) {
        List<Integer> topoOrder = TopoSort.kahn(dag).getOrder();
        int n = dag.size();
        int[] dist = new int[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }
        dist[src] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (var e : dag.getEdges(u)) {
                    if (dist[e.v] > dist[u] + e.w) {
                        dist[e.v] = dist[u] + e.w;
                        prev[e.v] = u;
                    }
                }
            }
        }

        return new PathResult(dist, prev);
    }
}
