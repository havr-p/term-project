package graphs;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;

public class PathDecompositionFlow extends NowhereZeroFlow {
    List<List<Integer>> spanningTree;
    DirectedGraph spanningTreeGraph;
    //edge index mapped to flow value
    List<Pair<Edge, Integer>> spanningTreeFlow = new ArrayList<>();
    List<Pair<Edge, Integer>> notSpanningTreeFlow = new ArrayList<>();
    List<Integer> matrixRowToIndex = new ArrayList<>();
    Iterator<Integer> spanningTreeIndexIterator;


    PathDecompositionFlow(DirectedGraph graph, int maxFlowValue) {
        super(graph, maxFlowValue);
        graph.indexEdges();
        spanningTree = DFS(0);
        this.spanningTreeGraph = createGraphFromSpanningTree();
        this.spanningTreeIndexIterator = spanningTreeGraph.getIndexes().iterator();
        List<Edge> spanningTreeEdges = spanningTreeGraph.getEdgeList();
        List<Edge> notSpanTreeEdges = graph.getEdgeList();
        notSpanTreeEdges.removeAll(spanningTreeEdges);
        //indexing spanning tree edges for further processing
        for (Edge spanningTreeEdge : spanningTreeEdges) {
           spanningTreeFlow.add(new Pair<>(spanningTreeEdge, 1));
        }
        for (Edge notSpanningTreeEdge : notSpanTreeEdges) {
            notSpanningTreeFlow.add(new Pair<>(notSpanningTreeEdge, 1));
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
        double[][] dirMatrix = getDirectionMatrix();
        findNowhere0FlowsHelper(0, flows, dirMatrix);
    }
    private void findNowhere0FlowsHelper(int index, List<List<Pair<Edge, Integer>>> flows, double[][] dirMatrix) {
        if (index == this.spanningTreeGraph.getNumberOfEdges()) {
            if (CheckUtil.preservesFlow(spanningTreeGraph, spanningTreeFlow) && spanningTreeGraph.getNumberOfEdges() > 0) {
                System.out.println(spanningTreeFlow);
                if (this.spanningTreeGraph.getNumberOfEdges() == this.graph.getNumberOfEdges()) {
                    flows.add(Collections.unmodifiableList(deepCopyFlow(spanningTreeFlow)));
                } else {
                    double[] stArr = spanningTreeFlow.stream()
                            .mapToDouble(edgeIntegerPair -> edgeIntegerPair.getB().doubleValue())
                            .toArray();
                    System.out.println(Arrays.toString(stArr));
                    double[] nstArr = getNotSpanningTreeValues(stArr, dirMatrix);
                    if (nstIsNowhere0(nstArr)) {
                        flow = constructResultingFlow(spanningTreeFlow, nstArr, matrixRowToIndex);
                        if (nstIsNowhere0(nstArr) && CheckUtil.preservesFlow(graph, flow)) {
                            flows.add(Collections.unmodifiableList(deepCopyFlow(flow)));
                        }
                    }
                }
            }
            return;
        }

        for (int flowValue = 1; flowValue <= MAX_FLOW_VALUE; flowValue++) {
            spanningTreeFlow.get(index).setB(flowValue);
            findNowhere0FlowsHelper(index + 1, flows, dirMatrix);
        }
    }


    private DirectedGraph createGraphFromSpanningTree() {
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
        System.out.println(m);
        System.out.println(n);
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
            Edge e = notInSpanningTreeEdges.get(i);
            matrixRowToIndex.add(e.index);
            for (int j = 0; j < matrixData[i].length; j++) {
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

    private boolean nstIsNowhere0(double[] nstEdges) {
        for (double v:
             nstEdges) {
            if (v == 0) return false;
        }
        return true;
    }

    //suppose edges in flow and helping flow are indexed
    private List<Pair<Edge, Integer>> constructResultingFlow(List<Pair<Edge, Integer>> spanningTreeFlow,
                                        double[] nstEdges,
                                        List<Integer> matrixRowToIndex) {
        //get edges from helping flow and edges which are not in spanning tree
        List<Edge> allEdges = graph.getEdgeList();
        List<Pair<Edge, Integer>> resultFlow = new ArrayList<>(spanningTreeFlow);
        for (int i = 0; i < matrixRowToIndex.size(); i++
             ) {
            resultFlow.add(new Pair(allEdges.get(matrixRowToIndex.get(i)), nstEdges[i]));
        }
        return resultFlow;
    }
}
