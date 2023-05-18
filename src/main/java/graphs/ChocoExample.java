package graphs;

import com.google.ortools.algorithms.KnapsackSolver;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class ChocoExample {
    public static void main(String[] args) {

        IntVar[] left = new IntVar[3];
        IntVar[] right = new IntVar[3];

        Model model = new Model("sum");
        IntVar v0 = model.intVar("v0", 1, 4);
        IntVar v1 = model.intVar("v1", 1, 4);
        model.sum("sum", v0, v1);


        // to maximize X
        //model.setObjective(Model.MAXIMIZE, left[1]);
// or model.setObjective(Model.MINIMIZE, X); to minimize X
        Solver solver = model.getSolver();
        System.out.println(model);
        while(solver.solve()){
            // an improving solution has been found
            System.out.println(v0.getName() + ": " + v0.getValue());
            System.out.println(v1.getName() + ": " + v1.getValue());
        }
// the last solution found was optimal (if search completed)
    }
}
