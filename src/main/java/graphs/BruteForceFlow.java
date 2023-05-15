package graphs;

import java.util.*;

public class BruteForceFlow {
    private final int MAX_FLOW_VALUE;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    private final List<Map<Integer, List<Integer>>> flow = new ArrayList<>();
    private final Graph graph;


    public BruteForceFlow(Graph graph, int MAX_FLOW_VALUE) {
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            flow.add(new HashMap<>());
        }
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

    public int getEdgeFlow(Edge edge) {
        return flow.get(edge.from()).get(edge.to());
    }
    public void setEdgeFlow(int edgeIndex, int value) {
        //System.out.println("was " + flow);
        flow.get(edge.from()).replace(edge.to(), value);
        //System.out.println("now " + flow);
    }


    public void initializeFlow() {
        for (int from = 0; from < graph.getNumberOfVertices(); from++) {

            Map<Integer, Integer> outgoingFlow = new HashMap<>();
            for (Integer to :
                    graph.adjVertices(from)) {
                outgoingFlow.put(to, 1);
            }
            flow.set(from, outgoingFlow);
        }
    }
    private List<Map<Integer, List<Integer>>> deepCopyFlow(List<Map<Integer, List<Integer>>> original) {
        List<Map<Integer, List<Integer>>> copy = new ArrayList<>(original.size());
        for (Map<Integer, List<Integer>> map : original) {
            copy.add(new HashMap<>(map));
        }
        return copy;
    }



    //depends on map outgoingEdgesLabelling
    private boolean nowhere0() {
        return flow.stream()
                .noneMatch(vertexFlow -> vertexFlow.values(0));
    }

    //depends on map outgoingEdgesLabelling
    private boolean preservesFlow() {
        for (int from = 0; from < graph.getNumberOfVertices(); from++) {
            if (!graph.getIncomingEdges(from).isEmpty() &&
                    !graph.getAdjacentEdges(from).isEmpty()) {
                int inFlowSum = 0, outFlowSum = 0;

                for (Edge e :
                        graph.getAdjacentEdges(from)) {
                    outFlowSum += getEdgeFlow(e);
                }
                for (Edge e :
                        graph.getIncomingEdges(from)) {
                    inFlowSum += getEdgeFlow(e);
                }
                if (inFlowSum != outFlowSum) return false;
            }
        }
        return true;
    }


    /**
     * @param edgeIndex - vertex, from which we continue to compute required network flow
     */

    public void findNowhere0Flows(List<Edge> edges, int edgeIndex, List<List<Map<Integer, Integer>>> flows) {
        if (edgeIndex == this.graph.getNumberOfEdges()) {
            //flows.add(Collections.unmodifiableList(newFlow));
            if (preservesFlow() && nowhere0()) {
                System.out.println("flow is valid");
                System.out.println(flow);
                flows.add(Collections.unmodifiableList(deepCopyFlow(flow)));
                System.out.println(flows);
            }
            return;
        }

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
           setEdgeFlow(edges.get(edgeIndex), flowValue);
            findNowhere0Flows(edges, edgeIndex + 1, flows);
        }
    }

    private boolean isVertexVisited(int vertex) {
        return  !flow.get(vertex).isEmpty();
    }



    /**
     * Generates and returns a sequence of possible flow values for the given vertex based on the given capacity constraints and in-flow sum.
     */
    private Iterator<Map<Integer, Integer>> generateFlowValues(int inFlowSum, Map<Integer, Integer> capacityConstraints) {
        return new OutgoingLabelingIterator(MAX_FLOW_VALUE, inFlowSum, capacityConstraints);
    }

    /**
     * Sets the flow value for the given vertex in the flow network.
     */
    private void setVertexFlow(int vertex, int flowValue) {
        flow.get(vertex).replaceAll((to, flow) -> flowValue);
    }

    public int getMaxFlowValue() {
        return this.MAX_FLOW_VALUE;
    }

}
