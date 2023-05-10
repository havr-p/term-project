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
    List<List<Map<Integer, Integer>>>flows = new ArrayList<>();

    public int factorial(int n) {
       int fact = 1;
       for (int i = 2; i <= n; i++) fact*=i;
       return fact;
    }

    @Test
    public void simplestBactrackingFlowTest() {
        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(2, 4);
        flowNetworkInterface.addEdge(0, 1, 4);
       // BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface,  4);
        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface, 4);
        flow.findNowhere0Flows(flowNetworkInterface.getEdges(),0, flows);
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
        FlowNetworkInterface flowNetworkInterface2 = new FlowNetwork(3, 4);
        flowNetworkInterface2.addEdge(0, 1, 4);
        flowNetworkInterface2.addEdge(1, 2, 4);
        BruteForceFlow flow1 = new BruteForceFlow(flowNetworkInterface2,  4);
        flow1.findNowhere0Flows(flowNetworkInterface2.getEdges(),0, flows);
        assertThat(flows.size(), is(4));
        System.out.println(flows.get(0));
    }

    @Test
    public void BactrackingFlowTest2() {
        //square

        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(4, 4);
        flowNetworkInterface.addEdge(0, 1, 4);
        flowNetworkInterface.addEdge(0, 2, 4);
        flowNetworkInterface.addEdge(1, 3, 4);
        flowNetworkInterface.addEdge(2, 3, 4);
        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface,  4);

        flow.findNowhere0Flows(flowNetworkInterface.getEdges(),0, flows);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void BactrackingFlowTest3() {
        FlowNetworkInterface flowNetworkInterface2 = new FlowNetwork(6, 4);
        flowNetworkInterface2.addEdge(0, 1, 4);
        flowNetworkInterface2.addEdge(0, 2, 4);
        flowNetworkInterface2.addEdge(1, 3, 4);
        flowNetworkInterface2.addEdge(2, 4, 4);
        flowNetworkInterface2.addEdge(3, 5, 4);
        flowNetworkInterface2.addEdge(4, 5, 4);
        BruteForceFlow flow1 = new BruteForceFlow(flowNetworkInterface2,  4);
        flow1.findNowhere0Flows(flowNetworkInterface2.getEdges(), 0, flows);
        flows.forEach(System.out::println);
        assertThat(flows.size(), is(16));
    }
    @Test
    public void emptyFlowTest() {
        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(5, 4);
        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface,  4);
        flow.findNowhere0Flows(flowNetworkInterface.getEdges(), 0, flows);
        assertTrue(flows.isEmpty());
    }
    @Test
    public void minimalpossibleFlowTest() {
        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(4, 4);
        flowNetworkInterface.addEdge(0, 1, 4);
        flowNetworkInterface.addEdge(0, 2, 4);
        flowNetworkInterface.addEdge(1, 3, 4);
        flowNetworkInterface.addEdge(2, 3, 4);
        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface,  4);
        flow.findNowhere0Flows(flowNetworkInterface.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    //in that test graph will contain numVertices vertices and (numVertices - 2) * 2 edges.
    @Test
    public void parametrizedNetworkTest() {
        int numVertices = 9;
        int sink = numVertices - 1;
        int sourceFlowSum = numVertices - 1;
        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(numVertices, 4);

            for (int to = 1; to <= numVertices - 2; to++) {
                flowNetworkInterface.addEdge(0, to, 4);
                flowNetworkInterface.addEdge(to, sink, 4);
            }

        BruteForceFlow flow = new BruteForceFlow(flowNetworkInterface, 4);

        flow.findNowhere0Flows(flowNetworkInterface.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertEquals(flows.size(), factorial(sourceFlowSum - 1) /
                ((long) factorial(numVertices - 3) * (factorial(sourceFlowSum - (numVertices - 2)))));
    }

    //Seymour's 6-flow theorem: Every bridgeless graph has a 6-flow.
    @Test
    public void guaranteedFlow() {
        FlowNetworkInterface flowNetworkInterface2 = new FlowNetwork(6, 4);
        flowNetworkInterface2.addEdge(0, 1, 4);
        flowNetworkInterface2.addEdge(0, 2, 4);
        flowNetworkInterface2.addEdge(1, 3, 4);
        flowNetworkInterface2.addEdge(2, 4, 4);
        flowNetworkInterface2.addEdge(3, 5, 4);
        flowNetworkInterface2.addEdge(4, 5, 4);
        flowNetworkInterface2.addEdge(1, 2, 4);
        flowNetworkInterface2.addEdge(3, 4, 4);
        BruteForceFlow flow1 = new BruteForceFlow(flowNetworkInterface2,  5);
        flow1.findNowhere0Flows(flowNetworkInterface2.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

    //Jaeger's 4-flow theorem: Every 4-edge-connected graph has a 4-flow.
    @Test
    public void guaranteedFlow2() {
        FlowNetworkInterface flowNetworkInterface2 = new FlowNetwork(7, 3);
        int maxFlow = 3;
        //flowNetwork2 is the 4-edge-connected graph (critical graph with chromatic number = 5)
       flowNetworkInterface2.addEdge(0, 1, maxFlow);
       flowNetworkInterface2.addEdge(0, 2, maxFlow);
       flowNetworkInterface2.addEdge(0, 3, maxFlow);
       flowNetworkInterface2.addEdge(0, 4, maxFlow);
       flowNetworkInterface2.addEdge(1, 3, maxFlow);
       flowNetworkInterface2.addEdge(1, 4, maxFlow);
       flowNetworkInterface2.addEdge(1, 6, maxFlow);
       flowNetworkInterface2.addEdge(2, 3, maxFlow);
       flowNetworkInterface2.addEdge(2, 4, maxFlow);
       flowNetworkInterface2.addEdge(2, 5, maxFlow);
       flowNetworkInterface2.addEdge(3, 4, maxFlow);
       flowNetworkInterface2.addEdge(3, 5, maxFlow);
       flowNetworkInterface2.addEdge(3, 6, maxFlow);
       flowNetworkInterface2.addEdge(4, 5, maxFlow);
       flowNetworkInterface2.addEdge(4, 6, maxFlow);
       flowNetworkInterface2.addEdge(5, 6, maxFlow);

        BruteForceFlow flow1 = new BruteForceFlow(flowNetworkInterface2,  maxFlow);
        flow1.findNowhere0Flows(flowNetworkInterface2.getEdges(),0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

}
