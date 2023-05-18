package graphs;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

public class LPFlow extends NowhereZeroFlow {

    //IntVar flowVar = model.intVar("f", 1, MAX_FLOW_VALUE);
    private final int V;
    Model model = new Model("nowhere-zero 5-flow");
    List<List<List<IntVar>>> edgeVariables = new ArrayList<>();

    protected LPFlow(Graph graph, int MAX_FLOW_VALUE) {
        super(graph, MAX_FLOW_VALUE);
        this.V = graph.numOfVertices;
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
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        NowhereZeroFlow flow = new LPFlow(graph, 4);
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
        for (int from = 0; from < V; from++) {
            for (int to :
                    edges.get(from)) {
                int count = multi.get(from).getOrDefault(to, 0);
                String name = String.format("x_%d_%d_%d", from, to, count);
                multi.get(from).put(to, count + 1);
                IntVar edgeVar = model.intVar(name, 1, MAX_FLOW_VALUE);
               // System.out.println("next edge var  " + edgeVar);
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
            IntVar[] out = outgoingEdges.toArray(new IntVar[0]);
            IntVar[] in = ingoingEdges.toArray(new IntVar[0]);
            // System.out.println("in:\n" + Arrays.toString(in));
            //System.out.println("out:\n" + Arrays.toString(out));
            Constraint c = model.sum(out, "=", in);
            c.post();

        }
    }

    @Override
    public void findNowhere0Flows(List<List<Pair<Edge, Integer>>> flows) {
        setVariables();
        addFlowConstraints();
        //prepared to solve
        Solver solver = model.getSolver();
        //System.out.println(model);
        while (solver.solve()) {
            Solution solution = new Solution(model);
            solution.record();
            List<IntVar> vars = solution.retrieveIntVars(false);
            List<Pair<Edge, Integer>> flow = new ArrayList<>();
            for (int i = 0; i < vars.size(); i++) {
                IntVar v = vars.get(i);

            }
            //System.out.println(solver.getSearchState());
           /* System.out.println("________________SOLUTION________________");
            for (int i = 0; i < vars.size(); i++) {
                System.out.println(vars.get(i).getName() +
                        ":  " + vars.get(i).getValue());
            }
            System.out.println("__________________END__________________");*/
            // do something, e.g. print out variable values
        }
    }

}
