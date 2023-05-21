package graphs;

import java.util.ArrayList;
import java.util.List;

public abstract class NowhereZeroFlow {
    final int MAX_FLOW_VALUE;
    final DirectedGraph directedGraph;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    List<Pair<Edge, Integer>> flow;

    protected NowhereZeroFlow(DirectedGraph directedGraph, int maxFlowValue) {
        this.MAX_FLOW_VALUE = maxFlowValue;
        this.directedGraph = directedGraph;
        initializeFlow();
    }

    public void initializeFlow() {
        flow = new ArrayList<>();
        List<List<Integer>> adjLists = directedGraph.adjacentLists();
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


    //depends on map outgoingEdgesLabelling
    boolean preservesFlow() {
        for (int from = 0; from < directedGraph.getNumberOfVertices(); from++) {
            if (!directedGraph.getIncomingEdges(from).isEmpty() &&
                    !directedGraph.getAdjacentEdges(from).isEmpty()) {
                int inFlowSum = 0;
                int outFlowSum = 0;

                List<Edge> adjEdges = directedGraph.getAdjacentEdges(from);
                for (Pair<Edge, Integer> eval :
                        flow) {
                    if (adjEdges.contains(eval.getA())) outFlowSum += eval.getB();
                }

                List<Edge> inc = directedGraph.getIncomingEdges(from);
                for (Pair<Edge, Integer> eval :
                        flow) {
                    if (inc.contains(eval.getA())) inFlowSum += eval.getB();
                }

                if (inFlowSum != outFlowSum) return false;
            }
        }
        return true;
    }

    public int getMaxFlowValue() {
        return this.MAX_FLOW_VALUE;
    }

    public abstract void findNowhere0FlowsInDirected(List<List<Pair<Edge, Integer>>> flows);
    public abstract void findNowhere0FlowsInUndirected(List<List<Pair<Edge, Integer>>> flows);
}
