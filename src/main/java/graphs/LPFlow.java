package graphs;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

public class LPFlow extends NowhereZeroFlow {

    private final int V;
    Model model = new Model("nowhere-zero 5-flow");
    List<List<List<IntVar>>> edgeVariables = new ArrayList<>();

    protected LPFlow(Graph directedGraph, int MAX_FLOW_VALUE) {
        super(directedGraph, MAX_FLOW_VALUE);
        this.V = directedGraph.getNumberOfVertices();
        //System.out.println(V);
        for (int i = 0; i < V; i++) {
            edgeVariables.add(new ArrayList<>());
            for (int j = 0; j < V; j++) {
                edgeVariables.get(i).add(new ArrayList<>());
            }
        }
        //System.out.println(edgeVariables);
    }

    public static void main(String[] args) {
        DirectedGraph directedGraph = new DirectedGraph(2);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        NowhereZeroFlow flow = new LPFlow(directedGraph, 4);
        List<List<Pair<Edge, Integer>>> flows = new ArrayList<>();
        flow.findNowhere0Flows(flows);
    }

    private void setVariables() {
        List<List<Integer>> edges = graph.adjacentLists();
        List<Map<Integer, Integer>> multi = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            multi.add(new HashMap<>());
        }
        //System.out.println(edges);
        //System.out.println(multi);
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                int u = i;
                int w = edges.get(u).get(i);
                int count = multi.get(u).getOrDefault(w, 0);
                String name = String.format("x_%d_%d_%d", u, w, count);
                IntVar edgeVar;
                if (graph.isDirected())
                    edgeVar = model.intVar(name, 1, MAX_FLOW_VALUE);
                else {
                    edgeVar = model.intVar(name, -MAX_FLOW_VALUE, MAX_FLOW_VALUE);
                    model.arithm(edgeVar, "!=", 0).post();
                }
                edgeVariables.get(u).get(w).add(edgeVar);
                multi.get(u).put(w, count + 1);
            }
        }
    }


    private void addFlowConstraintsDirected() {
        for (int from = 0; from < V; from++) {
            List<IntVar> outgoingEdges = edgeVariables.get(from).stream().flatMap(Collection::stream).toList();
            List<IntVar> ingoingEdges = new ArrayList<>();
            for (int i = 0; i < V; i++) {
                ingoingEdges.addAll(edgeVariables.get(i).get(from));
            }
            if (outgoingEdges.isEmpty() || ingoingEdges.isEmpty()) continue;
            IntVar[] out = outgoingEdges.toArray(new IntVar[0]);
            IntVar[] in = ingoingEdges.toArray(new IntVar[0]);
            // System.out.println("in:\n" + Arrays.toString(in));
            //System.out.println("out:\n" + Arrays.toString(out));
            Constraint c = model.sum(out, "=", in);
            c.post();

        }
    }

    private void addFlowConstraintsUndirected() {
        System.out.println(graph.adjacentLists());
        for (int i = 0; i < edgeVariables.size(); i++) {
            System.out.println(edgeVariables.get(i));
            /*List<IntVar> left = new ArrayList<>();
            List<IntVar> right = new ArrayList<>();
            for (int j = 0; j < edgeVariables.get(i).size(); j++) {
                    if (i < j)
                        left.addAll(Collections.unmodifiableList(edgeVariables.get(i).get(j)));
                    else
                        right.addAll(Collections.unmodifiableList(edgeVariables.get(j).get(i)));
                }
            IntVar[] leftArr = left.toArray(new IntVar[0]);
            IntVar[] rightArr = right.toArray(new IntVar[0]);
            System.out.println(Arrays.toString(leftArr));
            System.out.println(Arrays.toString(rightArr));
             Constraint c = model.sum(leftArr, "=", rightArr);
            System.out.println(c);
            c.post();*/
            List<IntVar> sum = new ArrayList<>();
            List<IntVar> left = new ArrayList<>();
            List<IntVar> right = new ArrayList<>();
            for (int j = 0; j < edgeVariables.get(i).size(); j++) {
                for (int k = 0; k < edgeVariables.get(i).get(j).size(); k++) {
                    if (i < j) {
                        //sum.add(edgeVariables.get(i).get(j).get(k));
                        left.add(edgeVariables.get(i).get(j).get(k));
                    } else {
                        //sum.add(edgeVariables.get(j).get(i).get(k).neg().intVar());
                        right.add(edgeVariables.get(j).get(i).get(k));
                    }
                }
            }
            IntVar[] sumArr = sum.toArray(new IntVar[0]);
            IntVar[] leftArr = left.toArray(new IntVar[0]);
            IntVar[] rightArr = right.toArray(new IntVar[0]);
            model.sum(leftArr, "=", rightArr).post();
        }
    }

    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        setVariables();
        if (graph.isDirected()) {
            addFlowConstraintsDirected();
        } else {
            addFlowConstraintsDirected();
        }
        //prepared to solve
        Solver solver = model.getSolver();
        System.out.println(model);
        if (solver.solve()) {
            Solution solution = new Solution(model);
            solution.record();
            List<IntVar> vars = solution.retrieveIntVars(false);
            List<Pair<Edge, Integer>> flow = new ArrayList<>();
            for (int i = 0; i < vars.size(); i++) {
                IntVar v = vars.get(i);

            }
            System.out.println(solver.getSearchState());
            System.out.println("________________SOLUTION________________");
            for (int i = 0; i < vars.size(); i++) {
                System.out.println(vars.get(i).getName() +
                        ":  " + vars.get(i).getValue());
            }
            System.out.println("__________________END__________________");
            // do something, e.g. print out variable values
        }
    }
}
