package graphs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;

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
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph);
        List<List<Integer>> spanningTree = flow.getSpanningTree();
        boolean[] contains = new boolean[numVertices];
        for (int from = 0; from < spanningTree.size(); from++) {
            for (int to = 0; to < spanningTree.get(from).size(); to++) {
                if (!contains[from]) contains[from] = true;
                contains[to] = true;
            }
        }
        assertFalse(Arrays.asList(contains).contains(false));
    }
}
