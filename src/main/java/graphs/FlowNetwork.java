package graphs;


public class FlowNetwork extends Graph implements FlowNetworkInterface {
    private final Integer maxCapacity;

    /**
     * list of lists of capacities of particular edges.
     * Each element in list contains capacities of edges incident to a particular vertex.
     */

    public FlowNetwork(int numVertices, int maxCapacity) {
        super(numVertices);
        this.maxCapacity = maxCapacity;
    }


    public boolean addEdge(int from, int to, int capacity) {
        if (existsEdge(from, to)) {
            System.out.println("This edge already exists");
            return false;
        }
        this.addEdge(from, to);
        return true;
    }


    public int getCapacity(int from, int to) {
        int capacity = -1;
        for (Edge e :
                getEdges()) {
            if (e.from() == from && e.to() == to) {
                capacity = e.capacity();
            }
        }
        return capacity;
    }

    @Override
    public int maxCapacity() {
        return maxCapacity;
    }

    @Override
    public boolean setCapacity(int from, int to, int capacity) {
        for (Edge e:
             this.getEdges()) {
            if (e.from() == from && e.to() == to) e.setWeight(capacity);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int from = 0; from < getNumberOfVertices(); from++) {
            s.append("Edges from ").append(from).append(":\n");
            for (Integer to :
                    adjVertices(from)) {
                s.append("    ").append(from).append(" ").append(getCapacity(from, to)).append("> ").append(to).append('\n');
            }
        }
        return s.toString();
    }

}
