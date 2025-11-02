package util.dataGen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import graph.model.Graph;
import graph.model.Edge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Utility for generating multiple graph datasets for algorithm testing.
 *
 * Each dataset is saved as JSON in /data/{small,medium,large}/ folders.
 * JSON structure example:
 * {
 *   "directed": true,
 *   "n": 8,
 *   "edges": [
 *     {"u": 0, "v": 1, "w": 3},
 *     {"u": 1, "v": 2, "w": 2}
 *   ],
 *   "source": 0,
 *   "weight_model": "edge"
 * }
 */
public class DatasetGenerator {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ---------- JSON WRAPPER ----------

    private static class GraphJson {
        boolean directed;
        int n;
        List<Map<String, Integer>> edges = new ArrayList<>();
        int source = 0;
        String weight_model = "edge";
    }

    /**
     * Saves a given graph into a JSON file.
     */
    private static void saveGraphToJson(Graph graph, String filePath) throws IOException {
        GraphJson json = new GraphJson();
        json.directed = true;
        json.n = graph.size();

        for (int u = 0; u < graph.size(); u++) {
            for (Edge e : graph.getEdges(u)) {
                Map<String, Integer> edge = new HashMap<>();
                edge.put("u", e.u);
                edge.put("v", e.v);
                edge.put("w", e.w);
                json.edges.add(edge);
            }
        }

        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(json, writer);
        }
    }

    // ---------- SMALL DATASETS ----------

    /**
     * Small cyclic graph (8 nodes, single small cycle).
     * Used for testing cycle detection.
     */
    public static void generateSmallCyclic1() throws IOException {
        Graph g = new Graph(8, true);
        int[][] edges = {
                {0, 1, 3}, {1, 2, 2}, {2, 3, 4}, {3, 1, 1}, // cycle
                {4, 5, 2}, {5, 6, 5}, {6, 7, 1}
        };
        for (int[] e : edges) g.addEdge(e[0], e[1], e[2]);
        saveGraphToJson(g, "data/small/small_cyclic_1.json");
    }

    /**
     * Small DAG (7 nodes).
     * A simple directed acyclic graph.
     */
    public static void generateSmallDAG1() throws IOException {
        Graph g = new Graph(7, true);
        int[][] edges = {
                {0, 1, 1}, {1, 2, 2}, {2, 3, 3}, {2, 4, 2},
                {3, 4, 1}, {4, 5, 2}, {5, 6, 1}
        };
        for (int[] e : edges) g.addEdge(e[0], e[1], e[2]);
        saveGraphToJson(g, "data/small/small_dag_1.json");
    }

    /**
     * Small disconnected DAG (10 nodes).
     * Includes multiple separate chains.
     */
    public static void generateSmallDAG2() throws IOException {
        Graph g = new Graph(10, true);
        int[][] edges = {
                {0, 1, 2}, {1, 2, 1},
                {3, 4, 3}, {4, 5, 1},
                {6, 7, 2}, {2, 8, 1}, {5, 8, 2}, {7, 8, 1}, {8, 9, 2}
        };
        for (int[] e : edges) g.addEdge(e[0], e[1], e[2]);
        saveGraphToJson(g, "data/small/small_dag_2.json");
    }

    // ---------- MEDIUM DATASETS ----------

    /**
     * Medium graph with multiple SCCs (15 nodes).
     * Tests SCC algorithms on complex but manageable graphs.
     */
    public static void generateMediumMultipleSCC() throws IOException {
        Graph g = new Graph(15, true);
        int[][] edges = {
                {0, 1, 2}, {1, 2, 1}, {2, 0, 1},
                {3, 4, 2}, {4, 5, 1}, {5, 3, 2},
                {6, 7, 1}, {7, 6, 2},
                {0, 3, 2}, {2, 6, 1}, {4, 8, 2}, {7, 9, 1},
                {8, 10, 1}, {9, 10, 2}, {10, 11, 1}, {11, 12, 2},
                {12, 13, 1}, {13, 14, 2}, {1, 9, 1}, {5, 11, 2}
        };
        for (int[] e : edges) g.addEdge(e[0], e[1], e[2]);
        saveGraphToJson(g, "data/medium/medium_multiple_scc.json");
    }

    /**
     * Medium DAG (16 nodes).
     * Used for topological sorting and DAG traversal testing.
     */
    public static void generateMediumDAG() throws IOException {
        Graph g = new Graph(16, true);
        int[][] edges = {
                {0,1,2},{0,2,3},{1,3,1},{2,4,2},{3,5,3},{4,6,1},
                {5,7,2},{6,8,1},{7,9,2},{8,10,1},{9,11,3},
                {10,12,2},{11,13,1},{12,14,2},{13,15,1}
        };
        for (int[] e : edges) g.addEdge(e[0], e[1], e[2]);
        saveGraphToJson(g, "data/medium/medium_dag.json");
    }

    /**
     * Medium dense cyclic graph (18 nodes, many edges).
     * Used for performance testing with moderate size.
     */
    public static void generateMediumCyclicDense() throws IOException {
        Random rand = new Random();
        Graph g = new Graph(18, true);
        for (int u = 0; u < 18; u++) {
            for (int v = 0; v < 18; v++) {
                if (u != v && rand.nextDouble() < 0.25) {
                    g.addEdge(u, v, rand.nextInt(10) + 1);
                }
            }
        }
        saveGraphToJson(g, "data/medium/medium_cyclic_dense.json");
    }

    // ---------- LARGE DATASETS ----------

    /**
     * Large sparse DAG (20 nodes).
     * Used for testing large but acyclic structures.
     */
    public static void generateLargeSparseDAG() throws IOException {
        Graph g = new Graph(20, true);
        for (int i = 0; i < 19; i++) {
            g.addEdge(i, i + 1, (i % 5) + 1);
            if (i + 2 < 20 && i % 3 == 0) {
                g.addEdge(i, i + 2, 2);
            }
        }
        saveGraphToJson(g, "data/large/large_sparse_dag.json");
    }

    /**
     * Large dense cyclic graph (30 nodes).
     * High edge density for stress testing.
     */
    public static void generateLargeCyclicDense() throws IOException {
        Random rand = new Random();
        Graph g = new Graph(30, true);
        for (int u = 0; u < 30; u++) {
            for (int v = 0; v < 30; v++) {
                if (u != v && rand.nextDouble() < 0.3) {
                    g.addEdge(u, v, rand.nextInt(10) + 1);
                }
            }
        }
        saveGraphToJson(g, "data/large/large_cyclic_dense.json");
    }

    /**
     * Large mixed SCC graph (25 nodes).
     * Contains several strongly connected components and random links.
     */
    public static void generateLargeMixedSCC() throws IOException {
        Graph g = new Graph(25, true);
        int[][] scc1 = {{0,1,1},{1,2,2},{2,0,3}};
        int[][] scc2 = {{5,6,1},{6,7,2},{7,5,3}};
        int[][] scc3 = {{10,11,1},{11,12,2},{12,10,3}};
        for (int[] e : scc1) g.addEdge(e[0], e[1], e[2]);
        for (int[] e : scc2) g.addEdge(e[0], e[1], e[2]);
        for (int[] e : scc3) g.addEdge(e[0], e[1], e[2]);
        g.addEdge(2,5,1);
        g.addEdge(7,10,2);
        g.addEdge(12,15,3);
        g.addEdge(15,20,2);
        g.addEdge(20,22,1);
        g.addEdge(22,24,2);
        saveGraphToJson(g, "data/large/large_mixed_scc.json");
    }

    // ---------- MAIN ----------

    public static void generateAll() throws IOException {
        // Small
        generateSmallCyclic1();
        generateSmallDAG1();
        generateSmallDAG2();
        // Medium
        generateMediumMultipleSCC();
        generateMediumDAG();
        generateMediumCyclicDense();
        // Large
        generateLargeSparseDAG();
        generateLargeCyclicDense();
        generateLargeMixedSCC();

        System.out.println("9 datasets generated under /data/{small,medium,large}/");
    }

    public static void main(String[] args) {
        try {
            generateAll();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
