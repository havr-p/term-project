package graphs;


import java.util.Collections;
import java.util.List;


public class BacktrackingFlow extends NowhereZeroFlow {


    public BacktrackingFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
    }



    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        findNowhere0FlowsHelper(0, flows);
    }


    /**
     * @param edgeIndex - vertex, from which we continue to compute required network flow
     */
    private void findNowhere0FlowsHelper(int edgeIndex, List<List<Pair<Edge, Integer>>> flows) {
        if (edgeIndex == this.graph.getNumberOfEdges()) {
            if (CheckUtil.preservesFlow(graph, flow) && graph.getNumberOfEdges() > 0) {
                // System.out.println("flow is valid");
                System.out.println(flow);
                flows.add(Collections.unmodifiableList(deepCopyFlow(flow)));
                //  System.out.println(flows);
            }
            return;
        }
        //System.out.println(flow);

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
            setEdgeFlow(edgeIndex, flowValue);
            findNowhere0FlowsHelper(edgeIndex + 1, flows);
        }
    }


}
