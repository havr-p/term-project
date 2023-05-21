package graphs;

import java.util.ArrayList;
import java.util.List;

public class PathDecompositionFlow {
    DirectedGraph directedGraph;

    PathDecompositionFlow(DirectedGraph directedGraph) {
        this.directedGraph = directedGraph;
    }

    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            graph.add(new ArrayList<>());
        }


        DirectedGraph flowNetwork = new DirectedGraph(4);
        flowNetwork.addEdge(0, 1);
        flowNetwork.addEdge(1, 2);
        flowNetwork.addEdge(1, 3);
        flowNetwork.addEdge(0, 2);
        flowNetwork.addEdge(2, 3);
        PathDecompositionFlow pathDecompositionFlow = new PathDecompositionFlow(flowNetwork);

        System.out.println(pathDecompositionFlow.getSpanningTree());
    }

    void DFSUtil(int v, boolean[] visited, List<List<Integer>> tree, int parent) {
        visited[v] = true;

        if (parent != -1) {
            tree.get(parent).add(v);
        }
        List<Integer> vertices = directedGraph.getAdjacentEdges(v).stream().map(Edge::to).toList();

        for (int n : vertices) {
            if (!visited[n]) {
                DFSUtil(n, visited, tree, v);
            }
        }
    }

    List<List<Integer>> DFS(int v) {
        boolean[] visited = new boolean[directedGraph.getNumberOfVertices()];
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < directedGraph.getNumberOfVertices(); i++) {
            tree.add(new ArrayList<>());
        }

        DFSUtil(v, visited, tree, -1);
        return tree;
    }

    List<List<Integer>> getSpanningTree() {
        return DFS(0);
    }

}
