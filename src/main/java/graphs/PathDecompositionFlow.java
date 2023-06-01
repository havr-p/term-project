package graphs;

import java.util.ArrayList;
import java.util.List;

public class PathDecompositionFlow extends NowhereZeroFlow {
    List<List<Integer>> spanningTree;


    PathDecompositionFlow(Graph graph, int maxFlowValue) {
        super(graph, maxFlowValue);
        spanningTree = DFS(0);
    }


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
        return this.spanningTree;
    }

    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        Graph spanningTreeGraph = createGraphFromSpanningTree();
        super.initializeFlow(); // need to reinitialize flow for the new graph
        super.findNowhere0FlowsHelper(0, flows, spanningTreeGraph);
    }

    private Graph createGraphFromSpanningTree() {
        Graph spanningTreeGraph = new DirectedGraph(this.graph.getNumberOfVertices());
        for (int from = 0; from < this.spanningTree.size(); from++) {
            for (int to : this.spanningTree.get(from)) {
                spanningTreeGraph.addEdge(from, to);
            }
        }

        return spanningTreeGraph;
    }


    /**
     * in undirected graph - select any orientation of edges.
     * 0 for edge is not going to ST edge,
     * 1 - going out from,
     * -1 - going to
     */

    public double[][] getDirectionMatrix(Graph graph, List<List<Integer>> spanningTree) {
        List<Edge> spanningTreeEdges = new ArrayList<>();
        for (int from = 0; from < spanningTree.size(); from++) {
            for (int to :
                    spanningTree.get(from)) {
                spanningTreeEdges.add(new Edge(from, to));
            }
        }
        List<Edge> notInSpanningTreeEdges = graph.getEdgeList();
        notInSpanningTreeEdges.removeAll(spanningTreeEdges);
        double[][] matrixData = new double[notInSpanningTreeEdges.size()][spanningTree.size()];
        for (int i = 0; i < matrixData.length; i++) {
            for (int j = 0; j < matrixData[i].length; j++) {
                Edge e = notInSpanningTreeEdges.get(i);
                Edge se = spanningTreeEdges.get(j);
                matrixData[i][j] = orientation(e, se);
            }
        }
        return matrixData;
    }

    private double orientation(Edge e1, Edge e2) {
        if (e1.equals(e2)) throw new IllegalArgumentException("edge is in ST and outside ST");
        if (e1.from() == e2.to()) return -1;
        if (e1.to() == e2.from()) return 1;
        return 0;
    }
}
