package graphs;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathDecompositionFlowTest {

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
    List<List<Pair<Edge, Integer>>> flows = new ArrayList<>();

    @Before
    public void setUp() {
        flows.clear();
    }


    @Test
    public void _1_simplestBactrackingFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(2);
        directedGraph.addEdge(0, 1);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        System.out.println(flows.size());
        assertEquals(flow.getMaxFlowValue(), flows.size());
        int flowIndex = 0;
        for (int i = 1; i <= 4; i++) {
            List<Pair<Edge, Integer>> expectedFlow = List.of(new Pair<>(new Edge(0, 1), i));
            System.out.println(expectedFlow);
            System.out.println("got " + flows.get(flowIndex).toString());
            assertThat(flows.get(flowIndex++), CoreMatchers.equalTo(expectedFlow));
        }
    }

    @Test
    public void _2_BactrackingFlowTest1() {
        DirectedGraph directedGraph = new DirectedGraph(3);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 2);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 4);
        flow1.findNowhere0Flows(flows);
        assertThat(flows.size(), is(4));
        System.out.println(flows.get(0));
    }

    @Test
    public void _3_BactrackingFlowTest2() {
        //square

        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 4);

        flow.findNowhere0Flows(flows);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void _4_BactrackingFlowTest3() {
        DirectedGraph directedGraph = new DirectedGraph(6);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 4);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(4, 5);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 4);
        flow1.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void _5_emptyFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(5);
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        assertTrue(flows.isEmpty());
    }

    @Test
    public void _6_minimalpossibleFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    //Seymour's 6-flow theorem: Every bridgeless graph has a 6-flow.
    @Test
    public void _7_guaranteedFlow() {
        DirectedGraph directedGraph = new DirectedGraph(6);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 4);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(4, 5);
        directedGraph.addEdge(1, 2);
        directedGraph.addEdge(3, 4);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 5);
        flow1.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    @Test
    public void _8_simpleMultigraph2() {
        DirectedGraph directedGraph = new DirectedGraph(2);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 0);
        directedGraph.addEdge(1, 0);
        directedGraph.addEdge(1, 0);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 1);
        flow1.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _8_simpleMultigraph() {
        int[][] a = {{1, 1}, {0, 0}};
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 4);
        flow1.findNowhere0Flows(flows);
        assertEquals(1, flows.size());
    }

    @Test
    public void _9_simpleGraph() {
        int[][] a = {
                {1, 2}, {0, 2}, {0, 1}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 4);
        flow1.findNowhere0Flows(flows);
    }

    @Test
    public void _9_simpleGraph2() {
        int[][] a = {
                {1, 3, 4}, {0, 2, 4}, {1, 3, 5}, {0, 2, 5}, {0, 1, 5}, {2, 3, 4}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 1);
        flow.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _9_k33() {
        int[][] a = {
                {3, 4, 5}, {3, 4, 5}, {3, 4, 5}, {0, 1, 2}, {0, 1, 2}, {0, 1, 2}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 2);
        flow.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _9_k23() {
        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 0);
        directedGraph.addEdge(1, 0);
        directedGraph.addEdge(1, 0);
        directedGraph.indexEdges();
        PathDecompositionFlow flow1 = new PathDecompositionFlow(directedGraph, 2);
        flow1.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _9_Y2() {
        int[][] a = {
                {1, 2, 3}, {0, 2, 4}, {0, 1, 5}, {0, 4, 5}, {1, 3, 5}, {2, 3, 4}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 2);
        flow.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _9_PetersenGraph() {
        int[][] a = {
                {1, 4, 5}, {0, 2, 6}, {1, 3, 7}, {2, 4, 8}, {0, 3, 9},
                {0, 7, 8}, {1, 8, 9}, {2, 5, 9}, {3, 5, 6}, {4, 6, 7}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 2);
        flow.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

    @Test
    public void _9_flowerSnark2() {
        int[][] a = {
                {2, 1, 1}, {0, 3, 0}, {0, 4, 6}, {1, 5, 7}, {2, 7, 7},
                {6, 6, 3}, {2, 5, 5}, {4, 3, 4}
        };
        DirectedGraph directedGraph = new DirectedGraph(a);
        directedGraph.indexEdges();
        PathDecompositionFlow flow = new PathDecompositionFlow(directedGraph, 2);
        flow.findNowhere0Flows(flows);
        assertEquals(flows.size(), 1);
    }

}
