package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*TODO Na vytvorenie nahodnej siete treba pridat mechanizmus overenia, ci vysledny graf
    je suvisly a ci naozaj ziadna hrana nevchadza do zdroju a ziadna hrana z ustia nevychadza (vsetky cesty sa zacinaju
    v zdroje a sa koncia v usti)
    Nasobne hrany su zakazane
 */

public class AdjListsFlowNetwork extends AdjacencyListsGraph implements FlowNetworkInterface {
    //two vertices, representing the flow source and the flow sink
    private final Integer source;
    private final Integer sink;
    private final Integer maxCapacity;

    /**
     * list of lists of capacities of particular edges.
     * Each element in list contains capacities of edges incident to a particular vertex.
     */
    private final List<Map<Integer, Integer>> capacities;

    public AdjListsFlowNetwork(int numVertices, int maxCapacity, int source, int sink) {
        super(numVertices);
        this.maxCapacity = maxCapacity;
        this.source = source;
        this.sink = sink;
        capacities = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            capacities.add(new HashMap<Integer, Integer>());
        }
    }


    @Override
    public int source() {
        return source;
    }

    @Override
    public int sink() {
        return sink;
    }

    /**
     * Prida do sieti hranu, veducu z vrchola from do vrchola to s kapacitou capacity
     */

    @Override
    public boolean addEdge(int from, int to, int capacity) {
        if (existsEdge(from, to)) {
            System.out.println("This edge is already exists");
            return false;
        } else {
            addEdge(from, to);
            setCapacity(from, to, capacity);
        }
        return true;
    }



    @Override
    public int getCapacity(int from, int to) {
        if (existsEdge(from, to)) return capacities.get(from).get(to);
        else return 0;
    }

    @Override
    public int maxCapacity() {
        return maxCapacity;
    }

    @Override
    public boolean setCapacity(int from, int to, int capacity) {
        try {
            if (existsEdge(from, to)) {
                capacities.get(from).put(to, capacity);
            } else throw new IllegalArgumentException("This edge does not exist");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        System.out.println("source: " + this.source());
        System.out.println("sink: " + this.sink());
        for (int from = 0; from < getNumberOfVertices(); from++) {
            s.append("Edges from " + from + ":\n");
            for (Integer to :
                    adjVertices(from)) {
                s.append("    " + from + " " + getCapacity(from, to) + "> " + to + '\n');
            }
        }
        return s.toString();
    }

}
