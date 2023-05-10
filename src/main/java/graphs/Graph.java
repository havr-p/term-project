package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//todo return to double list of vertices
public class Graph implements DirectedGraph {
    List<List<Integer>> edges;
    int numOfVertices;


    /* Konstruktor, ktory ako parameter dostane prirodzene cislo numVertices
       a vytvori graf o numVertices vrcholoch a bez jedinej hrany: */
    public Graph(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            edges.add(new ArrayList<>());
        }
    }

    @Override
    public int getNumberOfVertices() {
        return numOfVertices;
    }

    @Override
    public int getNumberOfOutgoingEdges(int from) {
        return edges.get(from).size();
    }

    @Override
    public int getNumberOfEdges() {
        int result = 0;
        for (int from = 0; from < numOfVertices; from++) {
                result+=edges.get(from).size();
        }
        return result;
    }

    @Override
    public void addEdge(int from, int to) {
      edges.get(from).add(to);
    }

    @Override
    public boolean existsEdge(int from, int to) {
       return edges.get(from).contains(to);
        }

    @Override
    public List<Integer> adjVertices(int from) {
        return Collections.unmodifiableList(edges.get(from));
    }
    @Override
    public List<Edge> getAdjacentEdges(int from) {
        List<Edge> result = new ArrayList<>();
        for (Integer to:
             edges.get(from)) {
            result.add(new Edge(from ,to));
        }
        return result;
    }
    @Override
    public List<Edge> getIncomingEdges(int to) {
        List<Edge> result = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
            for (Integer v:
                 edges.get(from)) {
                if (v == to) result.add(new Edge(from, to));
            }
        }
        return result;
    }
    @Override
    public List<Edge> getEdges() {
        List<Edge> result = new ArrayList<>();
        for (int from = 0; from < numOfVertices; from++) {
            result.addAll(getAdjacentEdges(from));
        }
        return result;
    }
}

