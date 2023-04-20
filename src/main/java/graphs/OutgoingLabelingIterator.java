package graphs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This iterator allows to generate every possible labeling for outgoing edges of the vertex. Maximum number of outgoing
 * edges is 36 (due to labellingsString() method)
 */


public class OutgoingLabelingIterator implements Iterator<Map<Integer, Integer>> {
    private final int MAX_FLOW_VALUE;
    private final int inFlowSum;
    private final Map<Integer, Integer> outgoingEdgesLabelling = new HashMap<>();
    private final Map<Integer, Integer> capacityConstraints;
    private final int MAX_OPTION, RADIX;
    private int currentOption = 0;
    private final int neighboursCount;

    public OutgoingLabelingIterator(int MAX_FLOW_VALUE, int inFlowSum,
                                    Map<Integer, Integer> capacityConstraints) {
        this.MAX_FLOW_VALUE = MAX_FLOW_VALUE;
        this.inFlowSum = inFlowSum;
        for (Integer neighbour :
                capacityConstraints.keySet()) {
            outgoingEdgesLabelling.put(neighbour, 0);
        }

        this.neighboursCount = capacityConstraints.size();
        this.RADIX = MAX_FLOW_VALUE + 1;
        this.MAX_OPTION = (int) Math.pow(RADIX, neighboursCount) - 1;
        for (int deg = 0; deg < neighboursCount; deg++) {
            currentOption += (int) Math.pow(RADIX, deg);
        }
        this.capacityConstraints = capacityConstraints;
    }
    //depends on map outgoingEdgesLabelling
    private boolean constrained() {
        for (Integer key :
                outgoingEdgesLabelling.keySet()) {
            if (outgoingEdgesLabelling.get(key) > capacityConstraints.get(key)) return false;
        }
        return true;
    }
    //depends on map outgoingEdgesLabelling
    private boolean nowhere0() {
        return !outgoingEdgesLabelling.containsValue(0);
    }

    //depends on map outgoingEdgesLabelling
    private boolean vertexPreservesFlow() {
        return inFlowSum == getSumFromOption();
    }
    //RADIX cannot be greater than 36
    private String labellingsString() {
        return Integer.toString(currentOption, RADIX);
    }

    private void setOutgoingEdgesLabeling() {
        if (currentOption > MAX_OPTION) System.out.println("currentOption > MAX_OPTION");
        Stack<Integer> labeling = labellingsString().chars().mapToObj(Character::getNumericValue)
                .collect(Collectors.toCollection(Stack<Integer>::new));

        int padding = neighboursCount - labeling.size();
        for (int i = 0; i < padding; i++) labeling.add(0, 0);

        outgoingEdgesLabelling.replaceAll((t, v) -> labeling.pop());
    }

    private int getSumFromOption() {

        return outgoingEdgesLabelling.values()
                .stream()
                .mapToInt(Integer::intValue).sum();
    }

    @Override
    public boolean hasNext() {
        if (inFlowSum < neighboursCount) return false;

        while (currentOption < MAX_OPTION) {
            setOutgoingEdgesLabeling();

            if (constrained() && vertexPreservesFlow() && nowhere0()) {
                return true;
            } else {
                currentOption++;
            }
        }

        return false;
    }


    @Override
    public Map<Integer, Integer> next() {
        currentOption++;
        return outgoingEdgesLabelling;
    }

}