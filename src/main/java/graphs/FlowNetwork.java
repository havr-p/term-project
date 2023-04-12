package graphs;

public interface FlowNetwork extends Graph {

    boolean addEdge(int from, int to, int capacity);

    int getCapacity(int from, int to);

    int maxCapacity();

    boolean setCapacity(int from, int to, int capacity);
}
