package graphs;


import java.util.Collections;
import java.util.List;


public class BacktrackingFlow extends NowhereZeroFlow {


    public BacktrackingFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
    }


    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        findNowhere0FlowsHelper(0, flows);
    }

    public void findNowhere0FlowsHelper(int edgeIndex, List<List<Pair<Edge, Integer>>> flows, Graph graph) {
        if (edgeIndex == this.graph.getNumberOfEdges()) {
            if (CheckUtil.preservesFlow(graph, flow) && graph.getNumberOfEdges() > 0) {
                System.out.println(flow);
                flows.add(Collections.unmodifiableList(deepCopyFlow(flow)));
            }
            return;
        }

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
            setEdgeFlow(edgeIndex, flowValue);
            findNowhere0FlowsHelper(edgeIndex + 1, flows, graph);
        }
    }


    /**
     * @param edgeIndex - vertex, from which we continue to compute required network flow
     */
    public void findNowhere0FlowsHelper(int edgeIndex, List<List<Pair<Edge, Integer>>> flows) {
        findNowhere0FlowsHelper(edgeIndex, flows, this.graph);
    }


}
