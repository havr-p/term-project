package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyListsGraph implements DirectedGraph {
    /* Pre kazdy vrchol zoznam vrcholov, do ktorych z daneho vrcholu vedie hrana: */
    private final List<List<Integer>> adjLists;

    /* Pocet hran v grafe: */
    private int numEdges;

    /* Konstruktor, ktory ako parameter dostane prirodzene cislo numVertices
       a vytvori graf o numVertices vrcholoch a bez jedinej hrany: */
    public AdjacencyListsGraph(int numVertices) {
        adjLists = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            adjLists.add(new ArrayList<Integer>());
        }
        numEdges = 0;
    }

    @Override
    public int getNumberOfVertices() {
        return adjLists.size();
    }

    @Override
    public int getNumberOfOutgoingEdges(int from) {
        return adjLists.get(from).size();
    }

    @Override
    public int getNumberOfEdges() {
        return numEdges;
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (existsEdge(from, to)) {
            return false;
        } else {
            adjLists.get(from).add(to);
            numEdges++;
            return true;
        }
    }

    @Override
    public boolean existsEdge(int from, int to) {
        return adjLists.get(from).contains(to);
    }

    @Override
    public Iterable<Integer> adjVertices(int from) {
       // System.out.println(from);
        // Zoznam adjLists.get(from) "obalime" tak, aby sa nedal menit:
        return Collections.unmodifiableList(adjLists.get(from));
    }
}

