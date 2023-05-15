package graphs;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
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
        Graph graph = new Graph(numVertices);
            graph.addEdge(0,1);
            graph.addEdge(1,2);
            graph.addEdge(1,3);
            graph.addEdge(0,2);
            graph.addEdge(2,3);
            PathDecompositionFlow flow = new PathDecompositionFlow(graph);
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
