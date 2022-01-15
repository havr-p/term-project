package graphs;

import java.util.*;
import java.util.stream.Collectors;


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
       // System.out.println(outgoingEdgesLabeling);
        this.neighboursCount = capacityConstraints.size();
        this.RADIX = MAX_FLOW_VALUE + 1;
        this.MAX_OPTION = (int) Math.pow(RADIX, neighboursCount) - 1;
        for (int deg = 0; deg < neighboursCount; deg++) {
            currentOption += (int) Math.pow(RADIX, deg);
        }
        this.capacityConstraints = capacityConstraints;
    }
    //depends on map
    private boolean constrained() {
        for (Integer key :
                outgoingEdgesLabelling.keySet()) {
            if (outgoingEdgesLabelling.get(key) > capacityConstraints.get(key)) return false;
        }
        return true;
    }
//depends on map
    private boolean nowhere0() {
        return !outgoingEdgesLabelling.containsValue(0);
    }

    private boolean isNextOption() {
        return (currentOption < MAX_OPTION);
    }
    //depends on map
    private boolean vertexPreservesFlow() {
        return inFlowSum == getSumFromOption();
    }

    private String labellingsString() {
        return Integer.toString(currentOption, RADIX);
    }

    private void setOutgoingEdgesLabeling() {
       // char[] charLabellings = labellingsString().toCharArray();
       // int padding = neighboursCount - charLabellings.length;
        if (currentOption > MAX_OPTION) throw new UnsupportedOperationException();
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
        try {
            setOutgoingEdgesLabeling();
        } catch (UnsupportedOperationException exception) {
            return false;
        }
        //important! hasNext() may change outgoingEdgesLabeling

        //System.out.println(currentOption);
        while (currentOption < MAX_OPTION) {
            if (constrained() && vertexPreservesFlow() && nowhere0()
                ) {
                return true;
            } else {
                currentOption++;
                setOutgoingEdgesLabeling();
            }
        }
        return (constrained() && vertexPreservesFlow() && nowhere0());
    }


    @Override
    public Map<Integer, Integer> next() {
        if (currentOption <= MAX_OPTION) setOutgoingEdgesLabeling();
        currentOption++;

        return outgoingEdgesLabelling;
    }


}
