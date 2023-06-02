package graphs;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

public class LPFlow extends NowhereZeroFlow {

    private final int V;
    Model model = new Model("nowhere-zero 5-flow");
    List<List<List<IntVar>>> edgeVariables = new ArrayList<>();

    protected LPFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
        this.V = graph.getNumberOfVertices();
        for (int i = 0; i < V; i++) {
            edgeVariables.add(new ArrayList<>());
            for (int j = 0; j < V; j++) {
                edgeVariables.get(i).add(new ArrayList<>());
            }
        }
    }

    public static void main(String[] args) {
        DirectedGraph directedGraph = new DirectedGraph(4);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(0, 2);
        directedGraph.addEdge(1, 3);
        directedGraph.addEdge(2, 3);
        NowhereZeroFlow flow = new LPFlow(directedGraph, 4);
        List<List<Pair<Edge, Integer>>> flows = new ArrayList<>();
        flow.findNowhere0Flows(flows);
    }

    private void setVariablesDirected() {
        List<List<Integer>> edges = graph.adjacentLists();
        List<Map<Integer, Integer>> multi = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            multi.add(new HashMap<>());
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                int u = i;
                int w = edges.get(u).get(j);
                int count = multi.get(u).getOrDefault(w, 0);
                String name = String.format("x_%d_%d_%d", u, w, count);
                IntVar edgeVar;
                edgeVar = model.intVar(name, 1, MAX_FLOW_VALUE);
                edgeVariables.get(u).get(w).add(edgeVar);
                multi.get(u).put(w, count + 1);
            }
        }
    }

    private void setEdgeVariablesUndirected() {
        List<Map<Integer, Integer>> multi = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            multi.add(new HashMap<>());
        }

        for (int u = 0; u < V; u++) {
            List<List<Integer>> lists = graph.adjacentLists();
            for (int j = 0; j < lists.get(u).size(); j++) {
                int w = lists.get(u).get(j);
                if (u < w) {
                    int count = multi.get(u).getOrDefault(w, 0);
                    String name = String.format("x_%d_%d_%d", u, w, count);
                    //String oppositeName = String.format("x_%d_%d_%d", w, u, count);
                    multi.get(u).put(w, count + 1);
                    IntVar edgeVar = model.intVar(name, -MAX_FLOW_VALUE, MAX_FLOW_VALUE);
                    IntVar oppositeEdgeVar = edgeVar.neg().intVar();
                    model.arithm(edgeVar, "!=", 0).post();
                   // edgeVar.div(oppositeEdgeVar).
                    edgeVariables.get(u).get(w).add(edgeVar);
                    edgeVariables.get(w).get(u).add(oppositeEdgeVar);
                }
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
            if (!outgoingEdges.isEmpty() && !ingoingEdges.isEmpty()) {
                IntVar[] out = outgoingEdges.toArray(new IntVar[0]);
                IntVar[] in = ingoingEdges.toArray(new IntVar[0]);
                Constraint c = model.sum(out, "=", in);
                c.post();
            }

        }
    }

    private void addFlowConstraintsUndirected() {
        List<Set<Integer>> uniqueEdges = new ArrayList<>();
        for (int from = 0; from < V; from++) {
            uniqueEdges.add(new HashSet<>(graph.adjVertices(from)));
        }
        for (int u = 0; u < V; u++) {
            System.out.println("vertex vars: " + edgeVariables.get(u));
            List<IntVar> outgoingEdges = new ArrayList<>();
            List<IntVar> ingoingEdges = new ArrayList<>();
            for (int w : uniqueEdges.get(u)) {
                if (u < w)
                    outgoingEdges.addAll(Collections.unmodifiableList(edgeVariables.get(u).get(w)));
                    ingoingEdges.addAll(Collections.unmodifiableList(edgeVariables.get(w).get(u)));

            }
            if (!ingoingEdges.isEmpty()) {
                IntVar[] in = ingoingEdges.toArray(new IntVar[0]);
                IntVar[] out = outgoingEdges.toArray(new IntVar[0]);
                Constraint c = model.sum(out, "=", in);
                c.post();
            }
        }
    }


    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        if (graph.isDirected()) {
            setVariablesDirected();
            addFlowConstraintsDirected();
        } else {
            setEdgeVariablesUndirected();
            addFlowConstraintsUndirected();
        }
        //prepared to solve
        Solver solver = model.getSolver();
       /* solver.setSearch(Search.inputOrderLBSearch(
                edgeVariables.stream().flatMap(Collection::stream)
                        .flatMap(Collection::stream)
                        .toArray(IntVar[]::new)
        ));*/
        System.out.println(model);
        if (solver.solve()) {
            Solution solution = new Solution(model);
            solution.record();
            List<IntVar> vars = solution.retrieveIntVars(true);
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

    //may be used on graphs where you can get many solutions
    public Solution getSolution() {
        setVarsAndConstraints();
        Solver solver = model.getSolver();
        solver.setSearch(Search.defaultSearch(model));
        System.out.println(model);
        if (solver.solve()) {
            Solution s = new Solution(model);
            s.record();
            List<IntVar> vars = s.retrieveIntVars(true);
            System.out.println(vars);
            System.out.println(solver.getSearchState());
            System.out.println("________________SOLUTION________________");
            for (int i = 0; i < vars.size(); i++) {
                IntVar v = vars.get(i).intVar();
                System.out.println(v.getName() +
                        ":  " + s.getIntVal(v));
            }
            System.out.println("__________________END__________________");
            return s;
        } else System.out.println("cannot solve");
        return null;
    }

    private void setVarsAndConstraints() {
        System.out.println("i am in setVarsAndConstraints");
        if (graph.isDirected()) {
            setVariablesDirected();
            addFlowConstraintsDirected();
        } else {
            setEdgeVariablesUndirected();
            addFlowConstraintsUndirected();
        }
    }
}
