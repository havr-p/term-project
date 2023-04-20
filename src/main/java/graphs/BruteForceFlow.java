package graphs;

import java.util.*;

public class BruteForceFlow {
    private final int MAX_FLOW_VALUE;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    private final List<Map<Integer, Integer>> flow = new ArrayList<>();
    private final FlowNetworkInterface flowNetworkInterface;
    private int traversedVerticesCount;
    List<Boolean> traversedVertices;

    public BruteForceFlow(FlowNetworkInterface flowNetworkInterface, int MAX_FLOW_VALUE) {
        for (int i = 0; i < flowNetworkInterface.getNumberOfVertices(); i++) {
            flow.add(new HashMap<>());
        }
        this.flowNetworkInterface = flowNetworkInterface;
        this.traversedVertices = new ArrayList<>();
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
    public void setEdgeFlow(Edge edge, int value) {
        flow.get(edge.from()).replace(edge.to(), value);
    }


    public void initializeFlow() {
        for (int from = 0; from < flowNetworkInterface.getNumberOfVertices(); from++) {

            Map<Integer, Integer> outgoingFlow = new HashMap<>();
            for (Integer to :
                    flowNetworkInterface.adjVertices(from)) {
                outgoingFlow.put(to, 1);
            }
            flow.set(from, outgoingFlow);
        }
    }

    /**
     * sum of flows of edges pointing into vertex to
     */

    public int flowSumInVertex(int to) {
        int flowSum = 0;
        for (Map<Integer, Integer> edgeFlow :
                flow) {
            if (edgeFlow.containsKey(to)) {
                flowSum += edgeFlow.get(to);
            }
        }
        return flowSum;
    }


   /* public boolean setVertexFlow(int from, Map<Integer, Integer> outgoingEdgesLabeling) {
        try {
            for (Integer to :
                    outgoingEdgesLabeling.keySet()) {
                if (flowNetworkInterface.existsEdge(from, to)) {
                    if (outgoingEdgesLabeling.get(to) <= flowNetworkInterface.getCapacity(e)) {
                        int flowValue = outgoingEdgesLabeling.get(to);
                        flow.get(from).put(to, flowValue);
                    } else {
                        throw new IllegalArgumentException("Wrong flow: flow cannot be greater than the capacity");
                    }
                } else {
                    throw new IllegalArgumentException("Edge does not exists");
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }*/

    private boolean constrained() {
        for (int vertexIndex = 0; vertexIndex < flowNetworkInterface.getNumberOfVertices(); vertexIndex++) {
          Map<Integer, Integer> vertexFlow = flow.get(vertexIndex);
            for (Map.Entry<Integer, Integer> edgeFlow:
                 vertexFlow.entrySet()) {
                if (flowNetworkInterface.getCapacity(vertexIndex, edgeFlow.getKey()) < edgeFlow.getValue())
                    return false;
            }
        }
        return true;
    }
    //depends on map outgoingEdgesLabelling
    private boolean nowhere0() {
        return flow.stream()
                .noneMatch(vertexFlow -> vertexFlow.containsValue(0));
    }

    //depends on map outgoingEdgesLabelling
    private boolean preservesFlow() {
        for (int from = 0; from < flowNetworkInterface.getNumberOfVertices(); from++) {
            if (!flowNetworkInterface.getIncomingEdges(from).isEmpty() &&
                    !flowNetworkInterface.getAdjacentEdges(from).isEmpty()) {
                int inFlowSum = 0, outFlowSum = 0;

                for (Edge e :
                        flowNetworkInterface.getAdjacentEdges(from)) {
                    outFlowSum += getEdgeFlow(e);
                }
                for (Edge e :
                        flowNetworkInterface.getIncomingEdges(from)) {
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

    public void findNowhere0Flows(int edgeIndex, List<List<Map<Integer, Integer>>> flows) {
        if (edgeIndex == this.flowNetworkInterface.getEdges().size()) {
            List<Map<Integer, Integer>> newFlow = new ArrayList<>(flow);
            //flows.add(Collections.unmodifiableList(newFlow));
            if (preservesFlow() && constrained() && nowhere0()) {
                System.out.println("flow is valid");
                System.out.println(newFlow);
                flows.add(Collections.unmodifiableList(newFlow));
            }
            return;
        }

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
           setEdgeFlow(this.flowNetworkInterface.getEdges().get(edgeIndex), flowValue);
            findNowhere0Flows( edgeIndex + 1, flows);
        }
    }

    private boolean isVertexVisited(int vertex) {
        return  !flow.get(vertex).isEmpty();
    }


    /**
     * Computes and returns the capacity constraints map for the given vertex.
     */
    private Map<Integer, Integer> getCapacityConstraints(int vertex) {
        Map<Integer, Integer> capacityConstraints = new HashMap<>();
        for (int to : flowNetworkInterface.adjVertices(vertex)) {
            capacityConstraints.put(to, flowNetworkInterface.getCapacity(vertex, to));
        }
        return capacityConstraints;
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
