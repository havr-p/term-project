package graphs;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;

public class PathDecompositionFlow extends NowhereZeroFlow {
    List<List<Integer>> spanningTree;
    Graph spanningTreeGraph;
    //edge index mapped to flow value
    List<Pair<Edge, Integer>> helperFlow = new ArrayList<>();


    PathDecompositionFlow(DirectedGraph graph, int maxFlowValue) {
        super(graph, maxFlowValue);
        graph.indexEdges();
        spanningTree = DFS(0);
        Graph spanningTreeGraph = createGraphFromSpanningTree();
        this.spanningTreeGraph = spanningTreeGraph;
        List<Edge> allEdges = graph.getEdgeList();
        List<Edge> spanningTreeEdges = spanningTreeGraph.getEdgeList();
        super.initializeFlow();
        //indexing spanning tree edges for further processing
        for (Edge spanningTreeEdge : spanningTreeEdges) {
           helperFlow.add(new Pair<>(spanningTreeEdge, 1));
        }
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

    }
    private void findNowhere0FlowsHelper(int edgeIndex, List<List<Pair<Edge, Integer>>> flows) {
        if (edgeIndex == this.spanningTreeGraph.getNumberOfEdges()) {
            if (CheckUtil.preservesFlow(spanningTreeGraph, helperFlow) && spanningTreeGraph.getNumberOfEdges() > 0) {
                System.out.println(helperFlow);

                flows.add(Collections.unmodifiableList(deepCopyFlow(helperFlow)));
            }
            return;
        }

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
            setEdgeFlow(edgeIndex, flowValue);
            findNowhere0FlowsHelper(edgeIndex + 1, flows);
        }
    }

    private Graph createGraphFromSpanningTree() {
        DirectedGraph st = new DirectedGraph(this.graph.getNumberOfVertices());
        int i = 0;
        for (int from = 0; from < this.spanningTree.size(); from++) {
            for (int to : this.spanningTree.get(from)) {
                st.addEdge(from, to, i);
            }
        }

        return st;
    }

    private double[] getNotSpanningTreeValues(double[] spanningTreeArr, double[][] directionMatrix) {
        RealMatrix m = MatrixUtils.createRealMatrix(directionMatrix);
        RealMatrix n = MatrixUtils.createRowRealMatrix(spanningTreeArr);
        RealMatrix notST = m.multiply(n);
        System.out.println(notST);
        return notST.getRow(0);
    }


    public double[][] getDirectionMatrix() {
        List<Edge> spanningTreeEdges = spanningTreeGraph.getEdgeList();
        List<Edge> notInSpanningTreeEdges = graph.getEdgeList();
        notInSpanningTreeEdges.removeAll(spanningTreeEdges);
        double[][] matrixData = new double[notInSpanningTreeEdges.size()][spanningTreeGraph.getNumberOfEdges()];
        for (int i = 0; i < matrixData.length; i++) {
            for (int j = 0; j < matrixData[i].length; j++) {
                Edge e = notInSpanningTreeEdges.get(i);
                Edge se = spanningTreeEdges.get(j);
                matrixData[i][j] = orientation(e, se);
            }
        }
        return matrixData;
    }

    /**
     * in undirected graph - select any orientation of edges.
     * 0 for edge is not going to ST edge,
     * 1 - going out from,
     * -1 - going to
     */
    private double orientation(Edge e1, Edge e2) {
        if (e1.equals(e2)) throw new IllegalArgumentException("edge is in ST and outside ST");
        if (e1.from() == e2.to()) return -1;
        if (e1.to() == e2.from()) return 1;
        return 0;
    }

    //suppose edges in flow and helping flow are indexed
    private void constructResultingFlow() {
        //get edges from helping flow and edges which are not in spanning tree

    }
}
