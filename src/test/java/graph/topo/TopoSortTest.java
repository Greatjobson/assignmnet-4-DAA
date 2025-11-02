package graph.topo;

import graph.model.Graph;
import org.junit.jupiter.api.Test;
import util.metrics.Metrics;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TopoSort (Kahn's algorithm).
 */
public class TopoSortTest {

    @Test
    void testSimpleDAG() {
        Graph g = new Graph(6, true);
        g.addEdge(5, 2, 1);
        g.addEdge(5, 0, 1);
        g.addEdge(4, 0, 1);
        g.addEdge(4, 1, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 1, 1);

        TopoResult res = TopoSort.kahn(g);
        var order = res.getOrder();

        // 1. Check all vertices are present
        assertEquals(6, order.size(), "Topo order should contain all vertices");

        // 2. Check basic ordering constraints (e.g., 5 before 2, 2 before 3)
        assertTrue(order.indexOf(5) < order.indexOf(2));
        assertTrue(order.indexOf(2) < order.indexOf(3));
        assertTrue(order.indexOf(3) < order.indexOf(1));

        // 3. Check metrics are recorded
        Metrics m = res.getMetrics();
        assertTrue(m.getExecutionTime() > 0, "Execution time should be positive");
        assertTrue(m.getCounter("Edges processed") >= 6, "Should count at least 6 edges");
    }

    @Test
    void testIsolatedVertices() {
        Graph g = new Graph(3, true); // no edges
        TopoResult res = TopoSort.kahn(g);

        assertEquals(3, res.getOrder().size(), "All isolated vertices should be included");
        assertTrue(res.getMetrics().getExecutionTime() >= 0);
    }

    @Test
    void testSingleVertex() {
        Graph g = new Graph(1, true);
        TopoResult res = TopoSort.kahn(g);

        assertEquals(1, res.getOrder().size());
        assertEquals(0, res.getOrder().get(0));
    }

    @Test
    void testCycleThrowsException() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1); // cycle

        assertThrows(IllegalStateException.class, () -> TopoSort.kahn(g),
                "Cyclic graph should throw an exception");
    }
}
