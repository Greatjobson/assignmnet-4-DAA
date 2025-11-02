package graph.scc;

import graph.model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CondensationGraph — verifies correct construction of DAG
 * from strongly connected components (SCCs).
 */
public class CondensationGraphTest {

    @Test
    void testCondensationSimple() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1); // SCC: 0-1
        g.addEdge(2, 3, 1); // SCC: 2-3
        g.addEdge(1, 2, 5); // edge between two SCCs
        g.addEdge(3, 2, 1);

        SCCKosaraju algo = new SCCKosaraju(g);
        SCCResult res = algo.run();

        Graph dag = CondensationGraph.build(g, res);


        assertTrue(dag.size() <= res.size(),
                "Condensation DAG must not have more vertices than SCC count");

        int totalEdges = 0;
        for (int i = 0; i < dag.size(); i++) {
            totalEdges += dag.getEdges(i).size();
        }

        assertEquals(1, totalEdges,
                "There should be exactly one edge between the SCCs in the condensation DAG");
    }

    @Test
    void testNoCrossEdges() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1); // one big SCC

        SCCKosaraju algo = new SCCKosaraju(g);
        SCCResult res = algo.run();

        Graph dag = CondensationGraph.build(g, res);

        for (int i = 0; i < dag.size(); i++) {
            assertTrue(dag.getEdges(i).isEmpty(),
                    "Single SCC should result in a DAG with no edges");
        }
    }
}
