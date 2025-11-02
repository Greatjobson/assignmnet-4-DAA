package graph.scc;

import graph.model.Graph;
import util.metrics.*;

import java.util.*;

/**
 * Kosaraju's algorithm for finding Strongly Connected Components (SCCs).
 * Includes metrics collection: DFS visits and edge traversals.
 */
public class SCCKosaraju {

    private final Graph g;
    private boolean[] visited;
    private Deque<Integer> order;
    private final Metrics metrics;

    /**
     * Creates a new Kosaraju SCC finder.
     * @param g the input graph
     */
    public SCCKosaraju(Graph g) {
        this.g = g;
        this.metrics = new Metrics();
    }

    /**
     * Runs the Kosaraju algorithm and collects performance metrics.
     * @return the result of SCC decomposition
     */
    public SCCResult run() {
        metrics.startTimer();

        int n = g.size();
        visited = new boolean[n];
        order = new ArrayDeque<>();

        // 1 First DFS — collect finishing order
        for (int v = 0; v < n; v++) {
            if (!visited[v]) dfs1(v);
        }

        // 2️ Reverse graph
        Graph gr = g.getReversed();

        // 3️ Second DFS — find components
        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();
        int[] compOf = new int[n];
        Arrays.fill(compOf, -1);

        while (!order.isEmpty()) {
            int v = order.pop();
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(gr, v, comp, visited);
                for (int x : comp) compOf[x] = components.size();
                components.add(comp);
            }
        }

        metrics.stopTimer();
        return new SCCResult(components, compOf);
    }

    private void dfs1(int v) {
        metrics.incrementCounter("DFS1 visits");
        visited[v] = true;
        for (var e : g.getEdges(v)) {
            metrics.incrementCounter("DFS1 edges");
            if (!visited[e.v]) dfs1(e.v);
        }
        order.push(v);
    }

    private void dfs2(Graph gr, int v, List<Integer> comp, boolean[] visited) {
        metrics.incrementCounter("DFS2 visits");
        visited[v] = true;
        comp.add(v);
        for (var e : gr.getEdges(v)) {
            metrics.incrementCounter("DFS2 edges");
            if (!visited[e.v]) dfs2(gr, e.v, comp, visited);
        }
    }

    /** @return metrics collected during algorithm execution */
    public Metrics getMetrics() {
        return metrics;
    }
}
