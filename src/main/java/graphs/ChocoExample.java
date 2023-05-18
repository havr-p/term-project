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

        for (int i = 0; i < 3; i++) {
            left[i] = model.intVar(0, 3);
            right[i] = model.intVar(0, 3);
        }


        // Beware : call the post() method to save it
        model.sum(left, "=", right).post();

        // to maximize X
        model.setObjective(Model.MAXIMIZE, left[1]);
// or model.setObjective(Model.MINIMIZE, X); to minimize X
        Solver solver = model.getSolver();
        System.out.println(model);
        while(solver.solve()){
            // an improving solution has been found
            System.out.println(left[1].getValue());
        }
// the last solution found was optimal (if search completed)
    }
}
