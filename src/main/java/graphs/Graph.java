package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph implements DirectedGraph {
    List<Edge> edges;
    int numOfVertices;


    /* Konstruktor, ktory ako parameter dostane prirodzene cislo numVertices
       a vytvori graf o numVertices vrcholoch a bez jedinej hrany: */
    public Graph(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.edges = new ArrayList<>();
    }

    @Override
    public int getNumberOfVertices() {
        return numOfVertices;
    }

    @Override
    public int getNumberOfOutgoingEdges(int from) {
        int result = 0;
        for (Edge e:
             edges) {
            if (e.from() == from) result++;
        }
        return result;
    }

    @Override
    public int getNumberOfEdges() {
        return edges.size();
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (existsEdge(from, to)) {
            return false;
        } else {
            edges.add(new Edge(from, to));
            return true;
        }
    }

    @Override
    public boolean existsEdge(int from, int to) {
        for (Edge e: edges)
            if (e.from() == from && e.to() == to) return true;
        return false;
        }

    @Override
    public List<Integer> adjVertices(int from) {
       List<Integer> adjacent = new ArrayList<>();
        for (Edge e: edges
             ) {
            if (e.from() == from) adjacent.add(e.to());
        }
        return Collections.unmodifiableList(adjacent);
    }
    @Override
    public List<Edge> getEdges() {
        return edges;
    }
    @Override
    public List<Edge> getAdjacentEdges(int from) {
        return edges.stream().filter(e -> e.from() == from).toList();
    }
    @Override
    public List<Edge> getIncomingEdges(int to) {
        return edges.stream().filter(e -> e.to() == to).toList();
    }
}

