package graph.scc;

import graph.model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    void testSingleComponent() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        var scc = new SCCKosaraju(g).run();

        assertEquals(1, scc.size(), "The entire graph must be a single component.");
        assertEquals(3, scc.getComponents().get(0).size());
    }

    @Test
    void testTwoComponents() {
        Graph g = new Graph(4, true);
        // первая компонента: 0 ↔ 1
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        // вторая: 2 ↔ 3
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);

        var scc = new SCCKosaraju(g).run();

        assertEquals(2, scc.size(), "There should be two components");
        assertTrue(scc.sizes().contains(2), "Each component has 2 vertices");
    }

    @Test
    void testChainNoCycles() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);

        var scc = new SCCKosaraju(g).run();

        assertEquals(4, scc.size(), "Each vertex is a separate component");
        for (var comp : scc.getComponents()) {
            assertEquals(1, comp.size());
        }
    }

    @Test
    void testDisconnectedGraph() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 1);
        g.addEdge(2, 3, 1);


        var scc = new SCCKosaraju(g).run();

        assertEquals(5, scc.size(), "All vertices are isolated (no cycles)");
    }

    @Test
    void testMetricsCollected() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        SCCKosaraju algo = new SCCKosaraju(g);
        algo.run();

        var m = algo.getMetrics();
        assertTrue(m.getCounter("DFS1 visits") > 0);
        assertTrue(m.getCounter("DFS2 visits") > 0);
        assertTrue(m.getExecutionTime() > 0);
    }
}
