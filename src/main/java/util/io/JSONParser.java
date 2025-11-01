package util.io;

import graph.model.Graph;
import com.google.gson.*;
import java.io.FileReader;

/**
 * Utility parser to load a graph from a JSON file.
 *
 * <p>Expected JSON format:
 * <pre>
 * {
 *   "directed": true|false,
 *   "n": number_of_vertices,
 *   "edges": [
 *     { "u": vertex_1, "v": vertex_2, "w": weight },
 *     ...
 *   ]
 * }
 * </pre>
 *
 * <p>Provides a single static method {@link #loadGraph(String)} to read the file and
 * construct a {@link Graph} instance.
 */
public class JSONParser {
    /**
     * Loads a graph from the JSON file at the given path.
     *
     * @param path path to the JSON file; can be absolute or relative to the working directory
     * @return a {@link Graph} instance constructed from the file contents
     * @throws RuntimeException if an I/O or JSON parsing error occurs; the original exception
     *                          is preserved as the cause
     *
     * Example:
     * <pre>
     * Graph g = JSONParser.loadGraph("data/graph.json");
     * </pre>
     */
    public static Graph loadGraph(String path) {
        try (FileReader reader = new FileReader(path)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            boolean directed = obj.get("directed").getAsBoolean();
            int n = obj.get("n").getAsInt();

            Graph graph = new Graph(n, directed);

            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                int u = edge.get("u").getAsInt();
                int v = edge.get("v").getAsInt();
                int w = edge.get("w").getAsInt();
                graph.addEdge(u, v, w);
            }

            return graph;
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON: " + e.getMessage(), e);
        }
    }
}
