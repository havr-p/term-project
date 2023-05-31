package graphs;

import java.util.ArrayList;
import java.util.List;

public abstract class NowhereZeroFlow {
    final int MAX_FLOW_VALUE;
    final Graph graph;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    List<Pair<Edge, Integer>> flow;

    protected NowhereZeroFlow(Graph graph, int maxFlowValue) {
        this.MAX_FLOW_VALUE = maxFlowValue;
        this.graph = graph;
        initializeFlow();
    }

    public void initializeFlow() {
        flow = new ArrayList<>();
        List<List<Integer>> adjLists = graph.adjacentLists();
        for (int from = 0; from < adjLists.size(); from++) {
            for (Integer to :
                    adjLists.get(from)) {
                flow.add(new Pair<>(new Edge(from, to), 1));
            }
        }
    }

    List<Pair<Edge, Integer>> deepCopyFlow(List<Pair<Edge, Integer>> original) {
        List<Pair<Edge, Integer>> copy = new ArrayList<>(original.size());
        for (Pair<Edge, Integer> edgeEvaluation : original) {
            copy.add(new Pair<>(edgeEvaluation.getA(), edgeEvaluation.getB()));
        }
        return copy;
    }

    public void setEdgeFlow(int edgeIndex, int value) {
        //System.out.println("was " + flow);
        flow.get(edgeIndex).setB(value);
        //System.out.println("now " + flow);
    }


    public int getMaxFlowValue() {
        return this.MAX_FLOW_VALUE;
    }

    public abstract void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows);
    //only when we have edges u->v and also v->u (what it will be in case of undirected graph)?
    public boolean shouldAddFlowConstraint() {return false;}
}
