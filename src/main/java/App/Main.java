package App;

import graph.model.Graph;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;
import util.io.JSONParser;
import util.dataGen.DatasetGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main application entry point.
 *
 * Features:
 *  - Generate new datasets (small/medium/large)
 *  - Select dataset and analyze:
 *      * SCC (Kosaraju)
 *      * Condensation DAG
 *      * Topological sort
 *      * Shortest / Longest paths
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Graph Analyzer ===");
        System.out.println("1 - Generate new datasets");
        System.out.println("2 - Analyze existing datasets");
        System.out.print("Enter choice: ");
        int mainChoice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (mainChoice == 1) {
            try {
                System.out.println("\nGenerating datasets...");
                DatasetGenerator.generateAll();
                System.out.println(" Datasets successfully generated under /data/");
            } catch (IOException e) {
                System.err.println("Error during dataset generation: " + e.getMessage());
            }
            System.out.println("\nNow let's analyze them!");
        }

        // === Stage 0: File selection ===
        System.out.println("\nSelect dataset size:");
        System.out.println("1 - Small");
        System.out.println("2 - Medium");
        System.out.println("3 - Large");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        String basePath = "data/";
        String folder = switch (choice) {
            case 1 -> "small";
            case 2 -> "medium";
            case 3 -> "large";
            default -> {
                System.out.println("Invalid choice. Defaulting to small.");
                yield "small";
            }
        };

        File dir = new File(basePath + folder);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("⚠ No JSON files found in " + dir.getPath());
            System.out.println("Try generating datasets first.");
            return;
        }

        System.out.println("\nAvailable test files:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + " - " + files[i].getName());
        }
        System.out.print("Select file: ");
        int fileChoice = sc.nextInt();
        if (fileChoice < 1 || fileChoice > files.length) {
            System.out.println("Invalid choice. Exiting.");
            return;
        }

        String filePath = files[fileChoice - 1].getPath();
        System.out.println("\nLoading graph from: " + filePath);

        // === Stage 1: Load graph ===
        Graph g = JSONParser.loadGraph(filePath);
        System.out.println("Graph loaded. Vertices: " + g.size());

        // === Stage 2: Compute SCC ===
        System.out.println("\n--- Strongly Connected Components ---");
        SCCKosaraju scc = new SCCKosaraju(g);
        SCCResult result = scc.run();
        System.out.println(result);
        scc.getMetrics().printMetrics();

        // === Stage 3: Build condensation DAG ===
        System.out.println("\n--- Condensation DAG ---");
        Graph dag = CondensationGraph.build(g, result);
        System.out.println("Condensation DAG built. Vertices: " + dag.size());
        for (int u = 0; u < dag.size(); u++) {
            System.out.print(u + " -> ");
            dag.getEdges(u).forEach(e -> System.out.print(e.v + "(" + e.w + ") "));
            System.out.println();
        }

        // === Stage 4: Topological sort ===
        System.out.println("\n--- Topological Sort ---");
        TopoResult topo = TopoSort.kahn(dag);
        System.out.println(topo);
        topo.getMetrics().printMetrics();

        // === Stage 5: Shortest paths ===
        System.out.println("\n--- Shortest Paths (DAG) ---");
        PathResult shortest = DAGShortestPaths.computeShortest(dag, 0);
        for (int i = 0; i < shortest.getDist().length; i++)
            System.out.println("0 -> " + i + " = " + shortest.getDist()[i]);
        shortest.getMetrics().printMetrics();

        // === Stage 6: Longest paths ===
        System.out.println("\n--- Longest Paths (DAG) ---");
        PathResult longest = DAGLongestPath.computeLongest(dag, 0);
        for (int i = 0; i < longest.getDist().length; i++)
            System.out.println("0 -> " + i + " = " + longest.getDist()[i]);
        longest.getMetrics().printMetrics();

        System.out.println("\n Analysis complete");
    }
}
