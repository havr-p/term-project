package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UndirectedGraph implements Graph {
    List<List<Integer>> edges;
    public UndirectedGraph(int numOfVertices) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < numOfVertices; i++) {
            edges.add(new ArrayList<>());
        }
    }

    //for tests
    public UndirectedGraph(int[][] edges) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            this.edges.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                addEdge(i, edges[i][j]);
            }
        }
    }
    public UndirectedGraph(List<List<Integer>> edges) {
        this.edges = new ArrayList<>();
        for (int i = 0; i < edges.size(); i++) {
            this.edges.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                addEdge(i, edges.get(i).get(j));
            }
        }
    }
    @Override
    public int getNumberOfVertices() {
        return edges.size();
    }

    @Override
    public int getNumberOfOutgoingEdges(int from) {
        return edges.get(from).size();
    }

    @Override
    public int getNumberOfEdges() {
        int result = 0;
        for (int from = 0; from < edges.size(); from++) {
            result += edges.get(from).size();
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

    public List<Integer> adjVertices(int from) {
        return Collections.unmodifiableList(edges.get(from));
    }


    public List<Edge> getAdjacentEdges(int from) {
        List<Edge> res = new ArrayList<>();
        for (Integer to:
             adjVertices(from)) {
            res.add(new Edge(from, to));
        }
        return res;
    }


    public List<Edge> getIncomingEdges(int to) {
        List<Edge> result = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
            for (Integer to1:
                 edges.get(from)) {
                if (to1 == to) result.add(new Edge(from, to));
            }
        }
        return result;
    }


    public List<Edge> getEdgeList() {
        List<Edge> res = new ArrayList<>();
        for (int from = 0; from < edges.size(); from++) {
            for (Integer to:
                 edges.get(from)) {
                res.add(new Edge(from, to));
            }
        }
        return res;
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
        return Collections.unmodifiableList(edges);
    }

    @Override
    public boolean isDirected() {
        return false;
    }
}
