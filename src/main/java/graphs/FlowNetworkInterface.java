package graphs;

public interface FlowNetworkInterface extends DirectedGraph {
    int source();

    int sink();

    boolean addEdge(int from, int to, int capacity);

    int getCapacity(int from, int to);

    int maxCapacity();

    boolean setCapacity(int from, int to, int capacity);
}
