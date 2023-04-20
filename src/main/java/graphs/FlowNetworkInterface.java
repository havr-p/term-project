package graphs;

import java.util.List;

public interface FlowNetworkInterface extends GraphInterface {

    boolean addEdge(int from, int to, int capacity);

    int getCapacity(int from, int to);

    int maxCapacity();

    boolean setCapacity(int from, int to, int capacity);

}
