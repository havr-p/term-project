package graphs;

import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LPFlowTest {
    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void starting(final Description description) {
            String methodName = description.getMethodName();
            String className = description.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            System.out.printf("\nStarting test: %s in %s\n", methodName, className);
        }
    };
    List<List<Pair<Edge, Integer>>> flows = new ArrayList<>();

    @Before
    public void setUp() {
        flows.clear();
    }


    @Test
    public void simplestBactrackingFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(2);
        directedGraph.addEdge(0, 1);
        LPFlow flow = new LPFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        System.out.println(flows.size());
        assertEquals(flow.getMaxFlowValue(), flows.size());
        int flowIndex = 0;
        for (int i = 1; i <= 4; i++) {
            List<Pair<Edge, Integer>> expectedFlow = List.of(new Pair<>(new Edge(0, 1), i));
            System.out.println(expectedFlow);
            System.out.println("got " + flows.get(flowIndex).toString());
            assertThat(flows.get(flowIndex++), CoreMatchers.equalTo(expectedFlow));
        }
    }

    @Test
    public void BactrackingFlowTest1() {
        DirectedGraph directedGraph = new DirectedGraph(3);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 2);
        LPFlow flow = new LPFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        for (int i = 1; i < 5; i++) {
            List<Pair<Edge, Integer>> expectedFlow = List.of(new Pair<>(new Edge(0, 1), i),
                    new Pair<>(new Edge(1, 2), i));
            System.out.println(expectedFlow);
            System.out.println("got " + flows.get(i - 1).toString());
            assertThat(flows.get(i - 1), CoreMatchers.equalTo(expectedFlow));
        }
        assertThat(flows.size(), is(4));
        System.out.println(flows.get(0));
    }

    @Test
    public void BactrackingFlowTest2() {
        //square

        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        LPFlow flow = new LPFlow(directedGraph, 4);

        flow.findNowhere0Flows(flows);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void BactrackingFlowTest3() {
        DirectedGraph directedGraph = new DirectedGraph(6);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 4);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(4, 5);
        LPFlow flow = new LPFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertThat(flows.size(), is(16));
    }

    @Test
    public void emptyFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(5);
        LPFlow flow = new LPFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        assertTrue(flows.isEmpty());
    }

    @Test
    public void minimalpossibleFlowTest() {
        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        LPFlow flow = new LPFlow(directedGraph, 4);
        flow.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }


    //Seymour's 6-flow theorem: Every bridgeless graph has a 6-flow.
    @Test
    public void guaranteedFlow() {
        DirectedGraph directedGraph = new DirectedGraph(6);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 4);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(4, 5);
        directedGraph.addEdge(1, 2);
        directedGraph.addEdge(3, 4);
        LPFlow flow = new LPFlow(directedGraph, 5);
        flow.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

    //Jaeger's 4-flow theorem: Every 4-edge-connected graph has a 4-flow.
    @Test
    public void guaranteedFlow2() {
        DirectedGraph directedGraph = new DirectedGraph(7);
        int maxFlow = 4;
        //4-edge-connected graph (critical graph with chromatic number = 5)
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(0, 3);
        directedGraph.addEdge(0, 4);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(1, 4);
        directedGraph.addEdge(1, 6);
        directedGraph.addEdge(2, 3);
        directedGraph.addEdge(2, 4);
        directedGraph.addEdge(2, 5);
        directedGraph.addEdge(3, 4);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(3, 6);
        directedGraph.addEdge(4, 5);
        directedGraph.addEdge(4, 6);
        directedGraph.addEdge(5, 6);

        LPFlow flow = new LPFlow(directedGraph, maxFlow);
        flow.findNowhere0Flows(flows);
        flows.forEach(System.out::println);
        assertFalse(flows.isEmpty());
    }

    @Test
    public void simpleMultigraph() {
        int[][] a = {{1, 1}, {0, 0}};
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 3);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }

    @Test
    public void simpleGraph() {
        int[][] a = {
                {1, 2}, {0, 2}, {0, 1}
        };
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 4);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void simpleGraph2() {
        int[][] a = {
                {1, 3, 4}, {0, 2, 4}, {1, 3, 5}, {0, 2, 5}, {0, 1, 5}, {2, 3, 4}
        };
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        flow.findNowhere0Flows(flows);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void k33() {
        int[][] a = {
                {3, 4, 5}, {3, 4, 5}, {3, 4, 5}, {0, 1, 2}, {0, 1, 2}, {0, 1, 2}
        };
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        flow.findNowhere0Flows(flows);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void k23() {
        UndirectedGraph graph = new UndirectedGraph(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);
        graph.addEdge(1, 0);
        graph.addEdge(1, 0);
        graph.addEdge(1, 0);
        LPFlow flow = new LPFlow(graph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void Y2() {
        int[][] a = {
                {1, 2, 3}, {0, 2, 4}, {0, 1, 5}, {0, 4, 5}, {1, 3, 5}, {2, 3, 4}
        };
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void PetersenGraph() {
        int[][] a = {
                {1, 4, 5}, {0, 2, 6}, {1, 3, 7}, {2, 4, 8}, {0, 3, 9},
                {0, 7, 8}, {1, 8, 9}, {2, 5, 9}, {3, 5, 6}, {4, 6, 7}
        };
        UndirectedGraph graph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark2() {
        int[][] a = {
                {2,1,1}, {0,3,0}, {0,4,6}, {1,5,7}, {2,7,7},
                {6,6,3}, {2,5,5}, {4,3,4}
        };
        Graph graph = new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        flow.findNowhere0Flows(flows);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark3() {
        int[][] a = {
                {1, 2, 10}, {0, 3, 11}, {0, 4, 6}, {1, 5, 7}, {2, 5, 11},
                {3, 4, 10}, {2, 7, 8}, {3, 6, 8}, {6, 7, 9}, {8, 10, 11},
                {0, 5, 9}, {1, 4, 9}
        };
        UndirectedGraph directedGraph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(directedGraph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark4() {
        int[][] a = {
                {4,1,3}, {0,5,2}, {1,6,3}, {2, 7, 0}, {0,8,12},
                {1,9,13}, {2,10,14}, {3,11,15}, {4,13,15}, {12,5,14},
                {13, 6, 15}, {14,7,12}, {4,9,11}, {8,5,10}, {9,6,11},
                {10,7,8}
        };
        UndirectedGraph directedGraph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(directedGraph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark5() {
        int[][] a = {
                {2, 3, 6}, {2, 5, 8}, {0, 1, 15}, {0, 4, 10}, {3, 5, 16},
                {1, 4, 11}, {0, 7, 14}, {6, 8, 19}, {1, 7, 13}, {10, 11, 17},
                {3, 9, 13}, {5, 9, 14}, {13, 14, 18}, {8, 10, 12}, {6, 11, 12},
                {2, 16, 19}, {4, 15, 17}, {9, 16, 18}, {12, 17, 19}, {7, 15, 18}
        };
        UndirectedGraph directedGraph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(directedGraph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark7() {
        int[][] a = {
                {1, 6, 7}, {0, 2, 10}, {1, 3, 13}, {2, 4, 16}, {3, 5, 19},
                {4, 6, 22}, {0, 5, 25}, {0, 8, 9}, {7, 12, 26}, {7, 11, 27},
                {1, 11, 12}, {9, 10, 14}, {8, 10, 15}, {2, 14, 15}, {11, 13, 17},
                {12, 13, 18}, {3, 17, 18}, {14, 16, 20}, {15, 16, 21}, {4, 20, 21},
                {17, 19, 23}, {18, 19, 24}, {5, 23, 24}, {20, 22, 26}, {21, 22, 27},
                {6, 26, 27}, {8, 23, 25}, {9, 24, 25}
        };
        UndirectedGraph directedGraph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(directedGraph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark11() {
        int[][] a = {
                {11,1,10}, {0,12,2}, {1,13,3}, {2,14,4}, {3,15,5},
                {4,16,6}, {5,17,7}, {6,18,8}, {7,19,9}, {8, 20, 10},
                {9,21,0}, {0,22,33}, {1,23,34}, {2,24,35}, {3, 25, 36},
                {4,26,37}, {5,27,38}, {6,28,39}, {7,29,40}, {8,30,41},
                {9,31,42}, {10,32,43}, {11,34,43}, {33,12,35}, {34,13,36},
                {35,14,37}, {36,15,38}, {37,16,39}, {38,17,40}, {39,18,41},
                {40,19,42}, {41,20,43}, {42,21,33}, {11,23,32}, {22,12,24},
                {23,13,25}, {24,14,26}, {25,15,27}, {26,16,28}, {27,17,29},
                {28,18,30}, {29,19,31}, {30,20,32}, {31,21,22}
        };
        UndirectedGraph directedGraph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(directedGraph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void flowerSnark15() {
        int[][] a = {
                {15,1,14}, {0,16,2}, {1,17,3}, {2,18,4}, {3,19,5},
                {4,20,6}, {5,21,7}, {6,22,8}, {7,23,9}, {8,24,10},
                {9,25,11}, {10,26,12}, {11,27,13}, {12,28,14}, {13,29,0},
                {0,30,45}, {1,31,46}, {2,32,47}, {3,33,48}, {4,34,49},
                {5,35,50}, {6,36,51}, {7,37,52}, {8,38,53}, {9,39,54},
                {10,40,55}, {11,41,56}, {12,42,57}, {13,43,58}, {14,44,59},
                {15,46,59}, {45,16,47}, {46,17,48}, {47,18,49}, {48,19,50},
                {49,20,51}, {50,21,52}, {51,22,53}, {52,23,54}, {53,24,55},
                {54,25,56}, {55,26,57}, {56,27,58}, {57,28,59}, {58,29,45},
                {15,31,44}, {30,16,32}, {31,17,33}, {32,18,34}, {33,19,35},
                {34,20,36}, {35,21,37}, {36,22,38}, {37,23,39}, {38,24,40},
                {39,25,41}, {40,26,42}, {41,27,43}, {42,28,44}, {43,29,30}
        };
        UndirectedGraph graph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void directedLPTest() {
        int[][] a = {
                {1, 2, 3},
                {0},
                {0},
                {0}
        };
        DirectedGraph graph =  new DirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 2);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
    @Test
    public void undirectedLPTest() {
        int[][] a = {
                {1, 2},
                {0},
                {0}
        };
        UndirectedGraph graph =  new UndirectedGraph(a);
        LPFlow flow = new LPFlow(graph, 1);
        Solution s = flow.getSolution();
        TestUtil.assertLPFlowIsAFlow(s);
        TestUtil.assertLPFlowNowhere0(s);
    }
}
