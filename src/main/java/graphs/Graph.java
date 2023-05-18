package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Graph implements DirectedGraph {
    List<List<Edge>> edges;
    int numOfVertices;


    public Graph(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            edges.add(new ArrayList<>());
        }
    }
    //for tests
    public Graph(int[][] edges) {
        this.numOfVertices = edges.length;
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            this.edges.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                int w = edges[i][j];
                addEdge(i, edges[i][j]);
            }
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
      edges.get(from).add(new Edge(from, to));
    }

    @Override
    public boolean existsEdge(int from, int to) {
            return edges.get(from).contains(new Edge(from, to));
        }

    @Override
    public List<Integer> adjVertices(int from) {
        return Collections.unmodifiableList(edges.get(from).stream().map(Edge::to).distinct().toList());
    }
    @Override
    public List<Edge> getAdjacentEdges(int from) {
        return Collections.unmodifiableList(edges.get(from));
    }
    @Override
    public List<Edge> getIncomingEdges(int to) {
        List<Edge> result = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
                 result.addAll(edges.get(from).stream().filter(edge -> to == edge.to()).toList());
        }
        return result;
    }
    @Override
    public List<Edge> getEdgeList() {
        return edges.stream().flatMap(Collection::stream).toList();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int from = 0; from < getNumberOfVertices(); from++) {
            s.append("Edges from ").append(from).append(":\n");
            for (Integer to :
                    adjVertices(from)) {
                s.append("    ").append(from).append("> ").append(to).append('\n');
            }
        }
        return s.toString();
    }

    public List<List<Integer>> adjacentLists() {
        List<List<Integer>> adjacentLists = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
            adjacentLists.add(Collections.unmodifiableList(edges.get(from).stream().
                    map(Edge::to)
                    .toList()));
        }
        return adjacentLists;
    }
}

