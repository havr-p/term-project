package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DirectedGraph implements Graph {
    List<List<Edge>> edges;
    int numOfVertices;


    public DirectedGraph(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            edges.add(new ArrayList<>());
        }
    }

    //for tests
    public DirectedGraph(int[][] edges) {
        this.numOfVertices = edges.length;
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            this.edges.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                addEdge(i, edges[i][j]);
            }
        }
    }

    public DirectedGraph(List<List<Integer>> edges, boolean indexed) {
        this.numOfVertices = edges.size();
        this.edges = new ArrayList<>();
        int ind = 0;
        for (int i = 0; i < numOfVertices; i++) {
            this.edges.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                if (!indexed) addEdge(i, edges.get(i).get(j));
                else addEdge(i, edges.get(i).get(j), ind++);
            }
        }
    }

    public void indexEdges() {
        int ind = 0;
        for (Edge e:
             getEdgeList()) {
            e.index = ind++;
        }
    }
    public List<Integer> getIndexes() {
        return getEdgeList().stream().map(Edge::index).collect(Collectors.toList());
    }


    public int getNumberOfVertices() {
        return numOfVertices;
    }


    public int getNumberOfOutgoingEdges(int from) {
        return edges.get(from).size();
    }


    public int getNumberOfEdges() {
        int result = 0;
        for (int from = 0; from < numOfVertices; from++) {
            result += edges.get(from).size();
        }
        return result;
    }


    public void addEdge(int from, int to) {
        edges.get(from).add(new Edge(from, to));
    }
    public void addEdge(int from, int to, int i) {
        edges.get(from).add(new Edge(from, to, i));
    }


    public boolean existsEdge(int from, int to) {
        return edges.get(from).contains(new Edge(from, to));
    }


    public List<Integer> adjVertices(int from) {
        return Collections.unmodifiableList(edges.get(from).stream().map(Edge::to).distinct().toList());
    }


    public List<Edge> getAdjacentEdges(int from) {
        return Collections.unmodifiableList(edges.get(from));
    }


    public List<Edge> getIncomingEdges(int to) {
        List<Edge> result = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
            result.addAll(edges.get(from).stream().filter(edge -> to == edge.to()).toList());
        }
        return result;
    }


    public List<Edge> getEdgeList() {
        return edges.stream().flatMap(Collection::stream).toList();
    }


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

    @Override
    public boolean isDirected() {
        return true;
    }
}

