package graphs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathDecompositionFlow {
    Graph graph;
    void DFSUtil(int v, boolean[] visited, List<List<Integer>> tree, int parent) {
        visited[v] = true;

        if (parent != -1) {
            tree.get(parent).add(v);
        }
        List<Integer> vertices = graph.getAdjacentEdges(v).stream().map(Edge::to).toList();

        for (int n : vertices) {
            if (!visited[n]) {
                DFSUtil(n, visited, tree, v);
            }
        }
    }

    List<List<Integer>> DFS(int v) {
        boolean[] visited = new boolean[graph.getNumberOfVertices()];
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            tree.add(new ArrayList<>());
        }

        DFSUtil(v, visited, tree, -1);
        return tree;
    }

    List<List<Integer>> getSpanningTree() {
        return DFS(0);
    }
    PathDecompositionFlow(Graph graph) {
        this.graph = graph;
    }


    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            graph.add(new ArrayList<>());
        }


        FlowNetwork flowNetwork = new FlowNetwork(4, 4);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(1, 2, 4);
        flowNetwork.addEdge(1, 3, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(2,3);
        PathDecompositionFlow pathDecompositionFlow = new PathDecompositionFlow(flowNetwork);

        System.out.println(pathDecompositionFlow.getSpanningTree());
    }

}
