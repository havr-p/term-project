package graphs;

import java.util.*;

/**
 * Trieda Flow reprezentuje celociselnu funkciu toku f: V x V -> Z, definovanu na mnozine hran E, pre ktoru plati:
 * 1. 0 =< f(v, u) <= capacity(v, u) pre ∀ (v, u) ∈ E
 * 2. ∑(f(u, w)) = 0, pre ∀ (u, w) ∈ E,  u != zdroj && u != ustie
 * 3. f(u, v) = -f(v, u);
 * <p>
 * Nikde nenulovy k-tok je take ohodnotenie hran sieti, kde kazda hrana je hodnotena cislom zo Zk\{0}.
 */


public class BacktrackingFlow {
    private final int MAX_FLOW_VALUE;
    //represents flow, maps neighbours of vertex to the values of flow pointing to neighbours
    private final List<Map<Integer, Integer>> flow = new ArrayList<>();
    private final FlowNetwork flowNetwork;

    public BacktrackingFlow(FlowNetwork flowNetwork, int sourceFlowSum, int MAX_FLOW_VALUE) {
        for (int i = 0; i < flowNetwork.getNumberOfVertices(); i++) {
            flow.add(new HashMap<>());
        }
        this.flowNetwork = flowNetwork;
        initializeFlow();
        this.MAX_FLOW_VALUE = MAX_FLOW_VALUE;
    }

    public boolean isNowhere0(List<Map<Integer, Integer>> flow) {
        for (Map<Integer, Integer> edgeFlow:
             flow) {
            if (edgeFlow.containsValue(0)) return false;
        }
        return true;
    }

    public int getEdgeFlow(int from, int to) {
        return flow.get(from).get(to);
    }


    public void initializeFlow() {
        for (int from = 0; from < flowNetwork.getNumberOfVertices(); from++) {

            Map<Integer, Integer> outgoingFlow = new HashMap<>();
            for (Integer to :
                    flowNetwork.adjVertices(from)) {
                outgoingFlow.put(to, 0);
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


    public boolean setVertexFlow(int from, Map<Integer, Integer> outgoingEdgesLabeling) {
        try {
            for (Integer to :
                    outgoingEdgesLabeling.keySet()) {
                if (flowNetwork.existsEdge(from, to)) {
                    if (outgoingEdgesLabeling.get(to) <= flowNetwork.getCapacity(from, to)) {
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
    }


    /**
     * @param from - vertex, from which we continue to compute required network flow
     */
    public void getNowhere0Flows(int from, List<List<Map<Integer, Integer>>> flowList) {
        int inFlowSum = flowSumInVertex(from);

    }

    /**
     * Computes and returns the capacity constraints map for the given vertex.
     */
    private Map<Integer, Integer> getCapacityConstraints(int vertex) {
        Map<Integer, Integer> capacityConstraints = new HashMap<>();
        for (int to : flowNetwork.adjVertices(vertex)) {
            capacityConstraints.put(to, flowNetwork.getCapacity(vertex, to));
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


    public void printFlow() {
        for (int from = 0; from < flowNetwork.getNumberOfVertices(); from++) {
            System.out.println("---" + from + "---");
            for (Integer to :
                    flowNetwork.adjVertices(from)) {
                System.out.println(from + " -> " + to + " : " + getEdgeFlow(from, to));
            }
        }
    }

}