package graphs;

import java.util.List;
import java.util.Set;

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
    public static boolean correctEdgeVariablesUndirected (List<Set<Integer>> lists) {
        for (int i = 0; i < lists.size(); i++) {
            for (int to:
                 lists.get(i)) {

            }
        }
        return false;
    }
}
