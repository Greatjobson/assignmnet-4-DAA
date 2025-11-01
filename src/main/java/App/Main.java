package App;
import graph.model.Graph;
import graph.scc.*;

public class Main {
    public static void main(String[] args) {
        Graph g = util.io.JSONParser.loadGraph("data/tasks.json");
        System.out.println("Graph loaded. Vertices: " + g.size());

        SCCKosaraju scc = new SCCKosaraju(g);
        SCCResult result = scc.run();
        System.out.println(result);

        Graph dag = CondensationGraph.build(g, result);
        System.out.println("Condensation DAG built. Vertices: " + dag.size());
        for (int u = 0; u < dag.size(); u++) {
            System.out.print(u + " -> ");
            dag.getEdges(u).forEach(e -> System.out.print(e.v + "(" + e.w + ") "));
            System.out.println();
        }
    }
}
