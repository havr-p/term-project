package graphs;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;
import java.util.stream.Collectors;

public class PathDecompositionFlow extends NowhereZeroFlow {
    List<List<Pair<Integer, Integer>>> spanningTree;
    DirectedGraph spanningTreeGraph;
    //edge index mapped to flow value
    List<Pair<Edge, Integer>> spanningTreeFlow = new ArrayList<>();
    List<Pair<Edge, Integer>> notSpanningTreeFlow = new ArrayList<>();
    List<Integer> matrixRowToIndex = new ArrayList<>();


    PathDecompositionFlow(DirectedGraph graph, int maxFlowValue) {
        super(graph, maxFlowValue);
        graph.indexEdges();
        spanningTree = DFS(0);
        this.spanningTreeGraph = createGraphFromSpanningTree();
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


    void DFSUtil(int v, boolean[] visited, List<List<Pair<Integer, Integer>>> tree, int parent) {
        visited[v] = true;

        List<Pair<Integer, Integer>> adjacentEdges = graph.getAdjacentEdges(v)
                .stream()
                .map(edge -> new Pair<>(edge.index(), edge.to()))
                .toList();

        for (Pair<Integer, Integer> edge : adjacentEdges) {
            if (!visited[edge.getB()]) {
                if (parent != -1) {
                    tree.get(parent).add(edge);
                } else {
                    tree.get(v).add(edge);
                }
                DFSUtil(edge.getB(), visited, tree, v);
            }
        }
    }

    List<List<Pair<Integer, Integer>>> DFS(int v) {
        boolean[] visited = new boolean[graph.getNumberOfVertices()];
        List<List<Pair<Integer, Integer>>> tree = new ArrayList<>();
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            tree.add(new ArrayList<>());
        }

        DFSUtil(v, visited, tree, -1);
        return tree;
    }

    List<List<Integer>> getSpanningTree() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < graph.getNumberOfVertices(); i++) {
            result.add(new ArrayList<>());
        }
        for (int i = 0; i < spanningTree.size(); i++) {
            for (Pair<Integer, Integer> edge : spanningTree.get(i)) {
                result.get(i).add(edge.getB());
            }
        }
        return result;
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
                        System.out.println("result flow");
                        System.out.println(flow);
                        if (nstIsNowhere0(nstArr) && CheckUtil.preservesFlow(graph, flow)) {
                            System.out.println("flow added");
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
        for (int from = 0; from < this.spanningTree.size(); from++) {
            for (var pair : this.spanningTree.get(from)) {
                st.addEdge(from, pair.getB(), pair.getA());
            }
        }
        return st;
    }

    private double[] getNotSpanningTreeValues(double[] spanningTreeArr, double[][] directionMatrix) {
        RealMatrix m = MatrixUtils.createRealMatrix(directionMatrix);
        RealMatrix n = MatrixUtils.createColumnRealMatrix(spanningTreeArr);
        System.out.println("m");
        System.out.println(m);
        System.out.println("n");
        System.out.println(n);
        RealMatrix notST = m.multiply(n);
        System.out.println("product");
        System.out.println(notST);
        return notST.getColumn(0);
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
            resultFlow.add(new Pair<>(allEdges.get(matrixRowToIndex.get(i)), (int) nstEdges[i]));
        }
        return resultFlow;
    }
}
