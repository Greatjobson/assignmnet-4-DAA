package App;
import graph.model.Graph;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;
import util.io.JSONParser;
/**
 * Main application entry point.
 * Loads a graph from JSON, computes strongly connected components (SCC),
 * builds the condensation DAG, prints DAG edges, and runs a topological sort.
 */
public class Main {
    /**
     * Program entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Stage 1: Load graph from JSON file
        Graph g = JSONParser.loadGraph("data/tasks.json");
        System.out.println("Graph loaded. Vertices: " + g.size());

        // Stage 2: Compute strongly connected components (SCC) using Kosaraju's algorithm
        SCCKosaraju scc = new SCCKosaraju(g);
        SCCResult result = scc.run();
        System.out.println(result);
        scc.getMetrics().printMetrics();

        // Stage 3: Build condensation DAG from SCC result
        Graph dag = CondensationGraph.build(g, result);
        System.out.println("Condensation DAG built. Vertices: " + dag.size());
        for (int u = 0; u < dag.size(); u++) {
            // Stage 3.1: Print outgoing edges for each DAG vertex
            System.out.print(u + " -> ");
            // e.v - destination vertex, e.w - edge weight
            dag.getEdges(u).forEach(e -> System.out.print(e.v + "(" + e.w + ") "));
            System.out.println();
        }

        // Stage 4: Perform topological sort on the DAG using Kahn's algorithm
        System.out.println("\nRunning topological sort...");
        TopoResult topoResult = TopoSort.kahn(dag);
        System.out.println(topoResult);
        topoResult.getMetrics().printMetrics();

        // Stage 5: Compute shortest paths from source 0 on the DAG
        // PathResult.getDist() returns distances array; unreachable vertices may be represented by 2147483648.
        PathResult shortest = DAGShortestPaths.computeShortest(dag, 0);
        System.out.println("Shortest distances: ");
        for (int i = 0; i < shortest.getDist().length; i++)
            System.out.println("0 -> " + i + " = " + shortest.getDist()[i]);
        shortest.getMetrics().printMetrics();

        // Stage 6: Compute longest paths from source 0 on the DAG
        // PathResult.getDist() returns distances array; unreachable vertices may be represented by -2147483648.
        PathResult longest = DAGLongestPath.computeLongest(dag, 0);
        System.out.println("\nLongest distances: ");
        for (int i = 0; i < longest.getDist().length; i++)
            System.out.println("0 -> " + i + " = " + longest.getDist()[i]);
        longest.getMetrics().printMetrics();
    }
}
