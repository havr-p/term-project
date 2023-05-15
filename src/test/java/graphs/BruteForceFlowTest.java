package graphs;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class BruteForceFlowTest {

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

    @Before
    public void setUp() {
        flows.clear();
    }
    List<List<Map<Integer, Integer>>> flows = new ArrayList<>();

    public int factorial(int n) {
       int fact = 1;
       for (int i = 2; i <= n; i++) fact*=i;
       return fact;
    }

    @Test
    public void simplestBactrackingFlowTest() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
       // BruteForceFlow flow = new BruteForceFlow(graph,  4);
        BruteForceFlow flow = new BruteForceFlow(graph, 4);
        flow.findNowhere0Flows(graph.getEdges(),0, flows);
        System.out.println(flows.size());
        assertEquals(flow.getMaxFlowValue(), flows.size());
        for (int i = 1; i <= 4; i++) {
            List<Map<Integer, Integer>> expectedFlow = List.of(Map.of(1, i), Map.of());
            System.out.println(expectedFlow);
            System.out.println("got " + flows.get(i - 1).toString());

            assertEquals(flows.get(i - 1).size(), expectedFlow.size());
            assertThat(flows.get(i - 1), CoreMatchers.equalTo(expectedFlow));
        }
    }
    @Test
    public void BactrackingFlowTest1() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        BruteForceFlow flow1 = new BruteForceFlow(graph,  4);
        flow1.findNowhere0Flows(graph.getEdges(),0, flows);
        assertThat(flows.size(), is(4));
        System.out.println(flows.get(0));
    }

    @Test
    public void BactrackingFlowTest2() {
        //square

        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        BruteForceFlow flow = new BruteForceFlow(graph,  4);

        flow.findNowhere0Flows(graph.getEdges(),0, flows);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void BactrackingFlowTest3() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        BruteForceFlow flow1 = new BruteForceFlow(graph,  4);
        flow1.findNowhere0Flows(graph.getEdges(), 0, flows);
        flows.forEach(System.out::println);
        assertThat(flows.size(), is(16));
    }
    @Test
    public void emptyFlowTest() {
        Graph graph = new Graph(5);
        BruteForceFlow flow = new BruteForceFlow(graph,  4);
        flow.findNowhere0Flows(graph.getEdges(), 0, flows);
        assertTrue(flows.isEmpty());
    }
    @Test
    public void minimalpossibleFlowTest() {
        Graph flowNetworkInterface = new Graph(4);
        flowNetworkInterface.addEdge(0, 1);
        flowNetworkInterface.addEdge(0, 2);
        flowNetworkInterface.addEdge(1, 3);
        flowNetworkInterface.addEdge(2, 3);
        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface,  4);
        flow.findNowhere0Flows(flowNetworkInterface.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    //Seymour's 6-flow theorem: Every bridgeless graph has a 6-flow.
    @Test
    public void guaranteedFlow() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        BruteForceFlow flow1 = new BruteForceFlow(graph,  5);
        flow1.findNowhere0Flows(graph.getEdges(),0, flows);
        flows.forEach(System.out::println);
        int[][] e = {
                {1,1}, {0, 0}
        };
        assertFalse(flows.isEmpty());
    }

    //Jaeger's 4-flow theorem: Every 4-edge-connected graph has a 4-flow.
    @Test
    public void guaranteedFlow2() {
        Graph graph = new Graph(7);
        int maxFlow = 3;
        //flowNetwork2 is the 4-edge-connected graph (critical graph with chromatic number = 5)
       graph.addEdge(0, 1);
       graph.addEdge(0, 2);
       graph.addEdge(0, 3);
       graph.addEdge(0, 4);
       graph.addEdge(1, 3);
       graph.addEdge(1, 4);
       graph.addEdge(1, 6);
       graph.addEdge(2, 3);
       graph.addEdge(2, 4);
       graph.addEdge(2, 5);
       graph.addEdge(3, 4);
       graph.addEdge(3, 5);
       graph.addEdge(3, 6);
       graph.addEdge(4, 5);
       graph.addEdge(4, 6);
       graph.addEdge(5, 6);

        BruteForceFlow flow1 = new BruteForceFlow(graph,  maxFlow);
        flow1.findNowhere0Flows(graph.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }
    @Test
    public void simpleMultigraph() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);
        graph.addEdge(1, 0);
        graph.addEdge(1, 0);
        graph.addEdge(1, 0);
        BruteForceFlow flow1 = new BruteForceFlow(graph,  2);
        flow1.findNowhere0Flows(graph.getEdges(), 0, flows);
        flows.forEach(System.out::println);
    }

}
