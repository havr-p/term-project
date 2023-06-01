package graphs;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtil {
    public static int factorial(int n) {
        int fact = 1;
        for (int i = 2; i <= n; i++) fact *= i;
        return fact;
    }

    public static void assertLPFlowNowhere0(Solution solution) {
        List<IntVar> vars = solution.retrieveIntVars(false);
        for (IntVar v :
                vars) {
            Assert.assertNotEquals(v.getValue(), 0);
        }
    }

    public static void assertNonLPFlowNowhere0(Graph graph, NowhereZeroFlow flow) {
        assertTrue(CheckUtil.preservesFlow(graph, flow.flow));
    }

    //check flow constraints for LP solution
    public static void assertLPFlowIsAFlow(Solution solution) {
        Map<Integer, Integer> flow = new HashMap<>();
        List<IntVar> vars = solution.retrieveIntVars(false);
        for (IntVar v :
                vars) {
            int vertex = parseVertexFromVar(v);
            int sum = flow.getOrDefault(vertex, 0) + v.getValue();
            flow.put(vertex, sum);
        }
        for (int val :
                flow.values()) {
            Assert.assertEquals(0, val);
        }
    }

    private static int parseVertexFromVar(IntVar var) {
        return Integer.parseInt(var.getName().split("_")[1]);
    }

    public static void assertAllVIncluded(List<List<Integer>> spanningTree, Graph g) {
        for (int i = 0; i < g.getNumberOfVertices(); i++) {
            assertTrue(spanningTree.get(i) != null);
        }
    }

    //A connected graph with V vertices and V-1 edges doesn't contain any cycles by definition
    public static void assertNotContainsCycles(List<List<Integer>> spanningTree, Graph g) {
        System.out.println(spanningTree);
        int edgeCount = spanningTree.stream().mapToInt(List::size).sum();
        assertEquals(g.getNumberOfVertices() - 1, edgeCount);
    }

}
