package graph.scc;

import graph.model.Graph;
import java.util.*;

public class SCCKosaraju {

    private final Graph g;
    private boolean[] visited;
    private Deque<Integer> order;

    public SCCKosaraju(Graph g) {
        this.g = g;
    }

    public SCCResult run() {
        int n = g.size();
        visited = new boolean[n];
        order = new ArrayDeque<>();

        // 1 first dfs
        for (int v = 0; v < n; v++) {
            if (!visited[v]) dfs1(v);
        }

        // 2 reverse graph
        Graph gr = g.getReversed();

        // 3️ second dfs
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

        return new SCCResult(components, compOf);
    }

    private void dfs1(int v) {
        visited[v] = true;
        for (var e : g.getEdges(v)) {
            if (!visited[e.v]) dfs1(e.v);
        }
        order.push(v);
    }

    private void dfs2(Graph gr, int v, List<Integer> comp, boolean[] visited) {
        visited[v] = true;
        comp.add(v);
        for (var e : gr.getEdges(v)) {
            if (!visited[e.v]) dfs2(gr, e.v, comp, visited);
        }
    }
}

