package graphs;

import org.junit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BacktrackingFlowTest {

    @Test
    public void simplestBactrackingFlowTest() {
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(2, 4, 0, 1);
        flowNetwork.addEdge(0, 1, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 1, 4);
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow.getNowhere0Flows(0, flows);
        assertEquals(1, flows.size());
        //System.out.println(" flow " + flows.get(0));
        assertTrue(List.of(Map.of(1, 1), Map.of()).containsAll(flows.get(0)));
        assertEquals(2, flows.get(0).size());
    }
    @Test
    public void BactrackingFlowTest1() {
        FlowNetworkInterface flowNetwork2 = new AdjListsFlowNetwork(3, 4, 0, 2);
        flowNetwork2.addEdge(0, 1, 4);
        flowNetwork2.addEdge(1, 2, 4);
        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork2, 4, 4);
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow1.getNowhere0Flows(0, flows);
        //System.out.println(flows);
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
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow.getNowhere0Flows(flowNetwork.source(), flows);
        //System.out.println(flows);
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
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow1.getNowhere0Flows(0, flows);
        //System.out.println(flows);
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
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow.getNowhere0Flows(flowNetwork.source(), flows);
        assertTrue(flows.isEmpty());
    }
    @Test
    public void minimalpossibleFlowTest() {
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(5, 4, 0, 4);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(1, 4, 4);
        flowNetwork.addEdge(2, 4, 4);
        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 3, 4);
        List<List<Map<Integer, Integer>>> flows = new ArrayList<>();
        flow.getNowhere0Flows(flowNetwork.source(), flows);
        assertFalse(flows.isEmpty());
    }
}
