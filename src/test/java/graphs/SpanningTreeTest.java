package graphs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.List;

import static org.junit.Assert.*;

public class SpanningTreeTest {
    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void starting(final Description description) {
            String methodName = description.getMethodName();
            String className = description.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            System.out.printf("\nStarting test: %s in %s\n", methodName, className);
        }
    };


    @Test
    public void simpleSpanningTreeTest() {
        int numVertices = 4;
        DirectedGraph directedGraph = new DirectedGraph(numVertices);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(2, 3);
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 1);
        List<List<Integer>> spanningTree = flow.getIndexedSpanningTree();
        boolean[] contains = new boolean[numVertices];
        for (int from = 0; from < spanningTree.size(); from++) {
            for (int to = 0; to < spanningTree.get(from).size(); to++) {
                if (!contains[from]) contains[from] = true;
                contains[to] = true;
            }
        }
        assertFalse(List.of(contains).contains(false));
    }

    @Test
    public void testSpanningTree_singleVertex() {
        List<List<Integer>> graph = List.of(
                List.of()
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> expectedTree = List.of(
                List.of()
        );
        assertEquals(expectedTree, new PathDecompositionFlow(g, 1).getIndexedSpanningTree());
    }

    @Test
    public void testSpanningTree_twoVertices() {
        List<List<Integer>> graph = List.of(
                List.of(1),
                List.of(0)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> expectedTree = List.of(
                List.of(1),
                List.of()
        );
        assertEquals(expectedTree, new PathDecompositionFlow(g, 1).getIndexedSpanningTree());
    }

    @Test
    public void testSpanningTree_lineGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1),
                List.of(0, 2),
                List.of(1)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        TestUtil.assertNotContainsCycles(spanningTree, g);
    }

    @Test
    public void testSpanningTree_triangleGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1, 2),
                List.of(0, 2),
                List.of(0, 1)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        TestUtil.assertNotContainsCycles(spanningTree, g);
    }

    @Test
    public void testSpanningTree_squareGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1, 3),
                List.of(0, 2),
                List.of(1, 3),
                List.of(0, 2)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        TestUtil.assertNotContainsCycles(spanningTree, g);
    }

    @Test
    public void testSpanningTree_pentagonGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1, 4),
                List.of(0, 2),
                List.of(1, 3),
                List.of(2, 4),
                List.of(0, 3)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        TestUtil.assertNotContainsCycles(spanningTree, g);
    }

    @Test
    public void testSpanningTree_complexGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1, 4, 5),
                List.of(0, 2, 6),
                List.of(1, 3, 7),
                List.of(2, 4, 8),
                List.of(0, 3, 9),
                List.of(0, 7, 8),
                List.of(1, 8, 9),
                List.of(2, 5, 9),
                List.of(3, 5, 6),
                List.of(4, 6, 7)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        TestUtil.assertNotContainsCycles(spanningTree, g);
    }

    @Test
    public void testSpanningTree_disconnectedGraph() {
        List<List<Integer>> graph = List.of(
                List.of(1),
                List.of(0),
                List.of(),
                List.of(4),
                List.of(3)
        );
        DirectedGraph g = new DirectedGraph(graph, false);
        List<List<Integer>> spanningTree = new PathDecompositionFlow(g, 1).getIndexedSpanningTree();
        TestUtil.assertAllVIncluded(spanningTree, g);
        int edgeCount = spanningTree.stream().mapToInt(List::size).sum();
        assertNotEquals(g.getNumberOfVertices() - 1, edgeCount);
    }

}
