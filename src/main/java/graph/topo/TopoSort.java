package graph.topo;

import graph.model.Graph;
import java.util.*;

public class TopoSort {

    public static TopoResult kahn(Graph dag) {
        long start = System.nanoTime();

        int n = dag.size();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++)
            for (var e : dag.getEdges(u))
                indeg[e.v]++;

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            if (indeg[i] == 0)
                q.add(i);

        List<Integer> order = new ArrayList<>();
        int operations = 0;

        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (var e : dag.getEdges(u)) {
                if (--indeg[e.v] == 0)
                    q.add(e.v);
                operations++;
            }
        }

        long time = System.nanoTime() - start;

        if (order.size() != n)
            throw new IllegalStateException("Graph is not a DAG!");

        return new TopoResult(order, time);
    }
}
