package App;



import graph.model.Graph;
import util.io.JSONParser;

public class Main {
    public static void main(String[] args) {
        Graph g = JSONParser.loadGraph("data/tasks.json");
        System.out.println("Graph loaded. Vertices: " + g.size());
        for (int u = 0; u < g.size(); u++) {
            System.out.print(u + " -> ");
            g.getEdges(u).forEach(e -> System.out.print(e.v + "(" + e.w + ") "));
            System.out.println();
        }
    }
}
