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
        this.edges.add(new Edge(from, to, capacity));
        return true;
    }


    public int getCapacity(int from, int to) {
        for (Edge e :
                getEdges()) {
            if (e.from() == from && e.to() == to) {
                return e.capacity();
            } else {
                throw new IllegalArgumentException("edge does not exist");
            }
        }
        return -1;
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
