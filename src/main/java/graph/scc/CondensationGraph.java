package graph.scc;

import graph.model.Graph;
import java.util.*;

public class CondensationGraph {

    public static Graph build(Graph original, SCCResult sccResult) {
        int compCount = sccResult.size();
        Graph dag = new Graph(compCount,true);

        int[] compOf = sccResult.getCompOfVertex();
        Set<String> seenEdges = new HashSet<>();

        for (int u = 0; u < original.size(); u++) {
            for (var e : original.getEdges(u)) {
                int cu = compOf[u];
                int cv = compOf[e.v];
                if (cu != cv) {
                    String key = cu + "-" + cv;
                    if (seenEdges.add(key)) {
                        dag.addEdge(cu, cv, e.w);
                    }
                }
            }
        }
        return dag;
    }
}
