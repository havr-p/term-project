package graphs;

import java.util.List;

public class CheckUtil {
    public static boolean preservesFlow(Graph graph, List<Pair<Edge, Integer>> flow) {
        for (int from = 0; from < graph.getNumberOfVertices(); from++) {
            if (!graph.getIncomingEdges(from).isEmpty() &&
                    !graph.getAdjacentEdges(from).isEmpty()) {
                int inFlowSum = 0;
                int outFlowSum = 0;

                List<Edge> adjEdges = graph.getAdjacentEdges(from);
                for (Pair<Edge, Integer> eval :
                        flow) {
                    if (adjEdges.contains(eval.getA())) outFlowSum += eval.getB();
                }

                List<Edge> inc = graph.getIncomingEdges(from);
                for (Pair<Edge, Integer> eval :
                        flow) {
                    if (inc.contains(eval.getA())) inFlowSum += eval.getB();
                }
                if (inFlowSum != outFlowSum) return false;
            }
        }
        return true;
    }

}
