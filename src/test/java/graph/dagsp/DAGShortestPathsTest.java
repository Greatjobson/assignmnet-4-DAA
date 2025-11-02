package graph.dagsp;

import graph.model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DAGShortestPaths.
 */
public class DAGShortestPathsTest {

    @Test
    void testSimpleShortestPaths() {
        Graph g = new Graph(6, true);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 6);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 4, 4);
        g.addEdge(2, 5, 2);
        g.addEdge(2, 3, 7);
        g.addEdge(3, 4, -1);
        g.addEdge(4, 5, -2);

        PathResult res = DAGShortestPaths.computeShortest(g, 0);
        int[] dist = res.getDist();

        // Expected shortest distances from 0
        assertEquals(0, dist[0]);
        assertEquals(5, dist[1]);
        assertEquals(3, dist[2]);
        assertEquals(10, dist[3]);
        assertEquals(7, dist[4]);
        assertEquals(5, dist[5]);

        assertTrue(res.getMetrics().getExecutionTime() > 0);
        assertTrue(res.getMetrics().getCounter("Edges processed") > 0);
        assertTrue(res.getMetrics().getCounter("Relaxations") > 0);
    }

    @Test
    void testDisconnectedVertex() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        // vertex 3 is disconnected

        PathResult res = DAGShortestPaths.computeShortest(g, 0);
        int[] dist = res.getDist();

        assertEquals(Integer.MAX_VALUE, dist[3], "Unreachable vertex should have MAX_VALUE distance");
    }
}
