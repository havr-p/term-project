package graphs;


import java.util.List;


public class BacktrackingFlow extends NowhereZeroFlow {


    public BacktrackingFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
    }


    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        findNowhere0FlowsHelper(0, flows);
    }


    /**
     * @param edgeIndex - vertex, from which we continue to compute required network flow
     */
    public void findNowhere0FlowsHelper(int edgeIndex, List<List<Pair<Edge, Integer>>> flows) {
        super.findNowhere0FlowsHelper(edgeIndex, flows, this.graph);
    }


}
