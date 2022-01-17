package graphs;

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


public class BacktrackingFlowTest {

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
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    @Test
    public void simplestBactrackingFlowTest() {
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(2, 4, 0, 1);
        flowNetwork.addEdge(0, 1, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 1, 4);
        flow.getNowhere0Flows(0, flows);
        assertEquals(1, flows.size());
        assertTrue(List.of(Map.of(1, 1), Map.of()).containsAll(flows.get(0)));
        assertEquals(2, flows.get(0).size());
    }
    @Test
    public void BactrackingFlowTest1() {
        FlowNetworkInterface flowNetwork2 = new AdjListsFlowNetwork(3, 4, 0, 2);
        flowNetwork2.addEdge(0, 1, 4);
        flowNetwork2.addEdge(1, 2, 4);
        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork2, 4, 4);
        flow1.getNowhere0Flows(0, flows);
        assertThat(flows.size(), is(1));
    }

    @Test
    public void BactrackingFlowTest2() {
        //square

        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(4, 4, 0, 3);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(1, 3, 4);
        flowNetwork.addEdge(2, 3, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 6, 4);

        flow.getNowhere0Flows(flowNetwork.source(), flows);
        assertThat(flows.size(), is(3));
    }

    @Test
    public void BactrackingFlowTest3() {
        FlowNetworkInterface flowNetwork2 = new AdjListsFlowNetwork(6, 4, 0, 5);
        flowNetwork2.addEdge(0, 1, 4);
        flowNetwork2.addEdge(0, 2, 4);
        flowNetwork2.addEdge(1, 3, 4);
        flowNetwork2.addEdge(2, 4, 4);
        flowNetwork2.addEdge(3, 5, 4);
        flowNetwork2.addEdge(4, 5, 4);
        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork2, 6, 4);
        flow1.getNowhere0Flows(0, flows);
        flows.forEach(System.out::println);
        assertThat(flows.size(), is(3));
    }
    @Test
    public void impossibleFlowTest() {
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(5, 4, 0, 4);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(0, 3, 4);
        flowNetwork.addEdge(1, 4, 4);
        flowNetwork.addEdge(2, 4, 4);
        flowNetwork.addEdge(3, 4, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 2, 4);
        flow.getNowhere0Flows(flowNetwork.source(), flows);
        assertTrue(flows.isEmpty());
    }
    @Test
    public void minimalpossibleFlowTest() {
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(4, 4, 0, 3);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(1, 3, 4);
        flowNetwork.addEdge(2, 3, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 3, 4);
        flow.getNowhere0Flows(flowNetwork.source(), flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    //in that test graph will contain numVertices vertices and (numVertices - 2) * 2 edges.
    @Test
    public void parametrizedNetworkTest() {
        int numVertices = 9;
        int sink = numVertices - 1;
        int sourceFlowSum = numVertices - 1;
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(numVertices, 4, 0, sink);

            for (int to = 1; to <= numVertices - 2; to++) {
                flowNetwork.addEdge(0, to, 4);
                flowNetwork.addEdge(to, sink, 4);
            }

        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, sourceFlowSum, 4);

        flow.getNowhere0Flows(flowNetwork.source(), flows);
        flows.forEach(System.out::println);
        assertEquals(flows.size(), factorial(sourceFlowSum - 1) /
                ((long) factorial(numVertices - 3) * (factorial(sourceFlowSum - (numVertices - 2)))));
    }

    //Seymour's 6-flow theorem: Every bridgeless graph has a 6-flow.
    @Test
    public void guaranteedFlow() {
        FlowNetworkInterface flowNetwork2 = new AdjListsFlowNetwork(6, 4, 0, 5);
        flowNetwork2.addEdge(0, 1, 4);
        flowNetwork2.addEdge(0, 2, 4);
        flowNetwork2.addEdge(1, 3, 4);
        flowNetwork2.addEdge(2, 4, 4);
        flowNetwork2.addEdge(3, 5, 4);
        flowNetwork2.addEdge(4, 5, 4);
        flowNetwork2.addEdge(1, 2, 4);
        flowNetwork2.addEdge(3, 4, 4);
        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork2, 4, 5);
        flow1.getNowhere0Flows(0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

    //Jaeger's 4-flow theorem: Every 4-edge-connected graph has a 4-flow.
    @Test
    public void guaranteedFlow2() {
        FlowNetworkInterface flowNetwork2 = new AdjListsFlowNetwork(7, 3, 0, 6);
        int maxFlow = 3;
        //flowNetwork2 is the 4-edge-connected graph (critical graph with chromatic number = 5)
       flowNetwork2.addEdge(0, 1, maxFlow);
       flowNetwork2.addEdge(0, 2, maxFlow);
       flowNetwork2.addEdge(0, 3, maxFlow);
       flowNetwork2.addEdge(0, 4, maxFlow);
       flowNetwork2.addEdge(1, 3, maxFlow);
       flowNetwork2.addEdge(1, 4, maxFlow);
       flowNetwork2.addEdge(1, 6, maxFlow);
       flowNetwork2.addEdge(2, 3, maxFlow);
       flowNetwork2.addEdge(2, 4, maxFlow);
       flowNetwork2.addEdge(2, 5, maxFlow);
       flowNetwork2.addEdge(3, 4, maxFlow);
       flowNetwork2.addEdge(3, 5, maxFlow);
       flowNetwork2.addEdge(3, 6, maxFlow);
       flowNetwork2.addEdge(4, 5, maxFlow);
       flowNetwork2.addEdge(4, 6, maxFlow);
       flowNetwork2.addEdge(5, 6, maxFlow);

        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork2, 6, maxFlow);
        flow1.getNowhere0Flows(0, flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

}
