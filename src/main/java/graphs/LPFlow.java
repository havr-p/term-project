package graphs;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.*;

public class LPFlow extends NowhereZeroFlow {
    //static { System.loadLibrary("jniortools");}

    Model model = new Model("nowhere-zero 5-flow");
    List<List<List<IntVar>>> edgeVariables = new ArrayList<>();
    IntVar flowVar = model.intVar("f", 1, MAX_FLOW_VALUE);
    private final int V;

    protected LPFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
        this.V = graph.numOfVertices;
        System.out.println(V);
        for (int i = 0; i < V; i++) {
            edgeVariables.add(new ArrayList<>());
            for (int j = 0; j < V; j++) {
                edgeVariables.get(i).add(new ArrayList<>());
            }
        }
        System.out.println(edgeVariables);
    }

    private void setVariables() {
        List<List<Integer>> edges = graph.adjacentLists();
        List<Map<Integer, Integer>> multi = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            multi.add(new HashMap<>());
        }
        System.out.println(edges);
        System.out.println(multi);
        for (int from = 0; from < V; from++) {
            for (int to :
                    edges.get(from)) {
                int count = multi.get(from).getOrDefault(to, 0);
                String name = String.format("x_%d_%d_%d", from, to, count);
                multi.get(from).put(to, count + 1);
                IntVar edgeVar = model.intVar(name, 1, MAX_FLOW_VALUE);
                model.arithm(edgeVar, "<=", MAX_FLOW_VALUE).post();
                model.arithm(edgeVar, ">=", 1).post();
                edgeVariables.get(from).get(to).add(edgeVar);
            }
        }
        //flowVar = model.intVar("flow", 1, MAX_FLOW_VALUE);
    }

    private void addFlowConstraints() {
        for (int from = 0; from < V; from++) {
            List<IntVar> outgoingEdges = edgeVariables.get(from).stream().flatMap(Collection::stream).toList();
            List<IntVar> ingoingEdges = new ArrayList<>();
            for (int i = 0; i < V; i++) {
                ingoingEdges.addAll(edgeVariables.get(i).get(from));
            }
            if (outgoingEdges.isEmpty() || ingoingEdges.isEmpty()) continue;
            String eq = "=";
            IntVar[] out = outgoingEdges.toArray(new IntVar[0]);
            IntVar[] in = ingoingEdges.toArray(new IntVar[0]);
            model.sum(out, "=", in).post();
        }
    }

    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        setVariables();
        addFlowConstraints();
        //prepared to solve
        Solver solver = model.getSolver();
        System.out.println(model);
        if (solver.solve()) {
            Solution solution = new Solution(model);
            List<IntVar> vars = solution.retrieveIntVars(false);
            System.out.println(solver.getSearchState());
            System.out.println("_____________SOLUTION_____________");
            for (int i = 0; i < vars.size(); i++) {
                System.out.print(vars.get(i).getName() +
                        ":  " + vars.get(i).getValue());
            }
            System.out.println("_____________END_____________");
            // do something, e.g. print out variable values
        } else if (solver.isStopCriterionMet()) {
            System.out.println("The solver could not find a solution nor prove that none exists in the given limits");
        } else {
            System.out.println("The solver has proved the problem has no solution");
        }
    }
/*
   List<List<List<MPVariable>>> edgeVariables = new ArrayList<>();
    //IntVar flowVar;
    MPSolver solver = MPSolver.createSolver("GLOP");
    MPVariable flowVar = solver.makeIntVar(1.0, MAX_FLOW_VALUE, "flow");
    private final int V;

    protected LPFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
        this.V = graph.numOfVertices;
        System.out.println(V);
        for (int i = 0; i < V; i++) {
            edgeVariables.add(new ArrayList<>());
            for (int j = 0; j < V; j++) {
                edgeVariables.get(i).add(new ArrayList<>());
            }
        }
        System.out.println(edgeVariables);
    }

    private void setVariables() {
        List<List<Integer>> edges = graph.adjacentLists();
        List<Map<Integer, Integer>> multi = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            multi.add(new HashMap<>());
        }
        System.out.println(edges);
        System.out.println(multi);
        for (int from = 0; from < V; from++) {
            for (int to :
                    edges.get(from)) {
                int count = multi.get(from).getOrDefault(to, 0);
                String name = String.format("x_%d_%d_%d", from, to, count);
                multi.get(from).put(to, count + 1);
                MPVariable edgeVar = solver.makeIntVar(1.0, MAX_FLOW_VALUE, name);
                MPConstraint boundC = solver.makeConstraint(1, MAX_FLOW_VALUE);
                boundC.setCoefficient(edgeVar, 1);
                edgeVariables.get(from).get(to).add(edgeVar);
            }
        }
        System.out.println("Number of variables = " + solver.numVariables());
    }

    private void addFlowConstraints() {
        double infinity = java.lang.Double.POSITIVE_INFINITY;
        for (int from = 0; from < V; from++) {
            List<MPVariable> outgoingEdges = edgeVariables.get(from).stream().flatMap(Collection::stream).toList();
            List<MPVariable> ingoingEdges = new ArrayList<>();
            List<Integer> adjVertices = graph.adjVertices(from);
            for (Integer to:
                    adjVertices) {
                ingoingEdges.addAll(edgeVariables.get(to).get(from));
            }
            if (outgoingEdges.isEmpty() || ingoingEdges.isEmpty()) continue;
            String eq = "=";
            MPVariable[] out = outgoingEdges.toArray(new MPVariable[0]);
            MPVariable[] in = ingoingEdges.toArray(new MPVariable[0]);
            MPConstraint sumC = solver.makeConstraint(0, 0);
            for (MPVariable inVar:
                 in) {
                sumC.setCoefficient(inVar, -1);
            }
            for (MPVariable outVar:
                    out) {
                sumC.setCoefficient(outVar, 1);
            }
        }
    }

    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        setVariables();
        addFlowConstraints();
        //prepared to solve
        //*System.out.println(solution);
        MPObjective objective = solver.objective();
        objective.setCoefficient(flowVar, 1);
        objective.setMinimization();
        final MPSolver.ResultStatus resultStatus = solver.solve();
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
            System.out.println("Objective value = " + objective.value());
            for (int i = 0; i < edgeVariables.size(); i++) {
                for (int j = 0; j < edgeVariables.get(i).size(); j++) {
                    for (int k = 0; k < edgeVariables.get(i).get(j).size(); k++) {
                        System.out.println(edgeVariables.get(i).get(j).get(k).name() + ":  " +
                                edgeVariables.get(i).get(j).get(k).solutionValue());
                    }
                }
            }
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }
    }*/

}
