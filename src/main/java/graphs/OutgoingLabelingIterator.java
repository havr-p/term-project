package graphs;

import java.util.*;


public class OutgoingLabelingIterator implements Iterator<Map<Integer, Integer>> {
    private final int MAX_FLOW_VALUE;
    private int inFlowSum;
    private Map<Integer, Integer> outgoingEdgesLabeling = new HashMap<>();
    private Map<Integer, Integer> capacityConstraints;
    private final int possibleOptionsCount;
    private int currentOption = 0;
    private final int neighboursCount;

    public OutgoingLabelingIterator(int MAX_FLOW_VALUE, int inFlowSum,
                                    Map<Integer, Integer> capacityConstraints) {
        this.MAX_FLOW_VALUE = MAX_FLOW_VALUE;
        this.inFlowSum = inFlowSum;
        for (Integer neighbour :
                capacityConstraints.keySet()) {
            outgoingEdgesLabeling.put(neighbour, 0);
        }
        this.neighboursCount = capacityConstraints.size();
        this.possibleOptionsCount = (int) Math.pow(MAX_FLOW_VALUE + 1, neighboursCount) - 1;
        this.capacityConstraints = capacityConstraints;
    }

    private boolean constrainedNowhere0() {
        for (Integer key:
             outgoingEdgesLabeling.keySet()) {
            if (outgoingEdgesLabeling.get(key) > capacityConstraints.get(key) ||
                outgoingEdgesLabeling.get(key) == 0) return false;
        }
        return true;
    }



    private void setOutgoingEdgesLabeling() {
        String labelings = Integer.toString(currentOption, MAX_FLOW_VALUE + 1);
        int padding = neighboursCount - labelings.length();
        Stack<Integer> labeling = new Stack<>();
        labeling.addAll(Collections.nCopies(padding, 0));

        //System.out.println(labelings + " " + currentOption);
        char[] charLabellings = labelings.toCharArray();
        for (char charLabelling : charLabellings) {
            labeling.add(Character.getNumericValue(charLabelling));
        }
        //System.out.println(Arrays.toString(labeling));
        outgoingEdgesLabeling.replaceAll((t, v) -> labeling.pop());
        //System.out.println(outgoingEdgesLabeling);
    }

    private int getSumFromOption() {
        String labelings = Integer.toString(currentOption, MAX_FLOW_VALUE + 1);
       // System.out.println(labelings);
        int sum = 0;
        for (int value :
                labelings.toCharArray()) {
            sum += Character.getNumericValue(value);
        }
        //System.out.println(sum + " " + inFlowSum);
        return sum;
    }

    @Override
    public boolean hasNext() {
        setOutgoingEdgesLabeling();
        //System.out.println(getSumFromOption());
        while ((currentOption < possibleOptionsCount)
                && !(getSumFromOption() == inFlowSum
                && constrainedNowhere0())) {
            currentOption++;
            setOutgoingEdgesLabeling();
        }
        return ((currentOption <= possibleOptionsCount)
                && (getSumFromOption() == inFlowSum)
                && constrainedNowhere0());
    }

    @Override
    public Map<Integer, Integer> next() {
        //get current labeling in a string
        //System.out.println("next call");
        //System.out.println(inFlowSum);
        currentOption++;
        return outgoingEdgesLabeling;
    }

    public static void main(String[] args) {
        OutgoingLabelingIterator iterator = new OutgoingLabelingIterator(4, 4,
                Map.of(1, 4, 2, 2, 3, 1));
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
