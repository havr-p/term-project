package graphs;


import java.util.*;

public class BruteForceFlow {
    private final int MAX_FLOW_VALUE;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    private List<Pair<Edge, Integer>> flow;
    private final Graph graph;


    public BruteForceFlow(Graph graph, int MAX_FLOW_VALUE) {
        flow = new ArrayList<>();
        this.graph = graph;
        this.MAX_FLOW_VALUE = MAX_FLOW_VALUE;
        initializeFlow();
    }

    public boolean isNowhere0(List<Map<Integer, Integer>> flow) {
        for (Map<Integer, Integer> edgeFlow :
                flow) {
            if (edgeFlow.containsValue(0)) return false;
        }
        return true;
    }

    public int getEdgeFlow(int edgeIndex) {
        return flow.get(edgeIndex).getB();
    }
    public void setEdgeFlow(int edgeIndex, int value) {
        //System.out.println("was " + flow);
        flow.get(edgeIndex).setB(value);
        //System.out.println("now " + flow);
    }


    public void initializeFlow() {
        List<List<Integer>> adjLists = graph.adjacentLists();
        for (int from = 0; from < adjLists.size(); from++) {
            for (Integer to:
                 adjLists.get(from)) {
                flow.add(new Pair<>(new Edge(from, to), 1));
            }
        }
    }
    private List<Pair<Edge, Integer>> deepCopyFlow(List<Pair<Edge, Integer>> original) {
        List<Pair<Edge, Integer>> copy = new ArrayList<>(original.size());
        for (Pair<Edge, Integer> edgeEvaluation : original) {
            copy.add(new Pair<>(edgeEvaluation.getA(), edgeEvaluation.getB()));
        }
        return copy;
    }




    //depends on map outgoingEdgesLabelling
    private boolean preservesFlow() {
        for (int from = 0; from < graph.getNumberOfVertices(); from++) {
            if (!graph.getIncomingEdges(from).isEmpty() &&
                    !graph.getAdjacentEdges(from).isEmpty()) {
                int inFlowSum = 0;
                int outFlowSum = 0;

                List<Edge> adjEdges = graph.getAdjacentEdges(from);
                for (Pair<Edge, Integer> eval:
                     flow) {
                    if (adjEdges.contains(eval.getA())) outFlowSum += eval.getB();
                }

                List<Edge> inc = graph.getIncomingEdges(from);
                for (Pair<Edge, Integer> eval:
                        flow) {
                    if (inc.contains(eval.getA())) inFlowSum += eval.getB();
                }

                if (inFlowSum != outFlowSum) return false;
            }
        }
        return true;
    }


    /**
     * @param edgeIndex - vertex, from which we continue to compute required network flow
     */

    public void findNowhere0Flows(List<Edge> edges, int edgeIndex, List<List<Pair<Edge, Integer>>> flows) {
        if (edgeIndex == this.graph.getNumberOfEdges()) {
            if (preservesFlow() && graph.getNumberOfEdges() > 0) {
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
            findNowhere0Flows(edges, edgeIndex + 1, flows);
        }
    }




    public int getMaxFlowValue() {
        return this.MAX_FLOW_VALUE;
    }

}
