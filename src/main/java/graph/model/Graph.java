package graph.model;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final Map<Integer, String> vertexNames = new HashMap<>();

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) adj.get(v).add(new Edge(v, u, w));
    }

    public void setVertexName(int id, String name) {
        vertexNames.put(id, name);
    }

    public String getVertexName(int id) {
        return vertexNames.getOrDefault(id, "V" + id);
    }

    public List<Edge> getEdges(int u) {
        return adj.get(u);
    }

    public int size() {
        return n;
    }

    public Graph getReversed() {
        Graph rev = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                rev.addEdge(e.v, e.u, e.w);
            }
        }
        return rev;
    }
}
