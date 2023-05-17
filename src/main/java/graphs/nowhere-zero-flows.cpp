#include <iostream>
#include <vector>
#include <cmath>
#include <lp_lib.h>
#include "lphelper.hpp" // Include the LPHelper class
#include <algorithm>
#include <set>
#include <sstream>
#include <string>


using namespace std;

void add_bound_constraints(const std::vector<std::vector<int>> &graph, LPHelper &lp,
                           vector<vector<vector<int>>>& edge_vars_x, vector<vector<vector<int>>>& edge_vars_y) {
    for (int i = 0; i < edge_vars_x.size(); ++i) {
        for (int j = 0; j < edge_vars_x[i].size(); ++j) {
             for (int k = 0; k < edge_vars_x[i][j].size(); ++k) {
                      int x = edge_vars_x[i][j][k];
                      int y = edge_vars_y[i][j][k];
                      //|x| >= 1
                      lp.add_constraint({1, -1}, {x, 1}, LE, -1);
                      lp.add_constraint({1, 1}, {x, 1}, GE, 1);
                      set_bounds(lp.get(), x, -6, 6);
                      lp.set_binary(y, TRUE);

                      lp.add_constraint({1, 6}, {x, y}, GE, 1);
                      lp.add_constraint({-1, -6}, {x, y}, GE, -5);
                  }

        }
    }

}


void printEdges(vector<vector<vector<int>>>& edge_vars_x){
    cout << "EDGE_VARS: " << endl;
    for(int i = 0; i < edge_vars_x.size(); i++){
        cout << "EDGES FROM: " << i << endl;
        for (int j = 0; j < edge_vars_x[i].size(); ++j) {
            for (int k = 0; k < edge_vars_x[i][j].size(); ++k) {
                cout<<"EDGE " << i<< " --- "<<j<<endl;
                cout<<"edge var: " << edge_vars_x[i][j][k] <<endl;
            }
        }
    }
}

void add_flow_constraints(const std::vector<std::vector<int>> &graph, LPHelper &lp, vector<string> &variable_names,
                          vector<vector<vector<int>>>& edge_vars) {
    for (int i = 0; i < edge_vars.size(); i++) {
        vector<int> indexes;
        vector<double> constraint_coefficients;
        for (int j = 0; j < edge_vars[i].size(); j++) {
            for (int k = 0; k < edge_vars[i][j].size(); k++) {
                if (i < j) {
                    indexes.push_back(edge_vars[i][j][k]);
                    constraint_coefficients.push_back(1);
                } else {
                    indexes.push_back(edge_vars[j][i][k]);
                    constraint_coefficients.push_back(-1);
                }
            }
        }
        lp.add_constraint(constraint_coefficients, indexes, EQ, 0);
    }
}



void read_adjacency_list(vector<vector<int>> &graph) {
    string line;
    int vertex = 0;

    while (getline(cin, line)) {
        if (line.empty()) {
            break;
        }

        istringstream iss(line);
        string value;
        while (getline(iss, value, ',')) {
            int other_vertex = stoi(value);
            graph[vertex].push_back(other_vertex);

        }

        vertex++;
    }
}

double rflow(const std::vector<std::vector<int>> &graph) {
    vector<string> variable_names;
    vector<vector<vector<int>>> edge_vars_x(graph.size(), vector<vector<int>>(graph.size(),vector<int>()));
    vector<vector<vector<int>>> edge_vars_y(graph.size(), vector<vector<int>>(graph.size(),vector<int>()));
    vector<std::unordered_map<int, int>> multi(graph.size());
    //create variables
    variable_names.emplace_back("r");
    int var_index = 2;
    for (int i = 0; i < graph.size(); i++) {
        for (int j = 0; j < graph[i].size(); j++) {
            int u = i, w = graph[i][j];
                string name_x = "x_" + to_string(u) + "_" + to_string(w);
                string name_y = "y_" + to_string(u) + "_" + to_string(w);
                name_x += "_" + to_string(multi[u][w]);
                name_y += "_" + to_string(multi[u][w]);
                variable_names.push_back(name_x);
                edge_vars_x[u][w].push_back(var_index++);
                variable_names.push_back(name_y);
                edge_vars_y[u][w].push_back(var_index++);
                multi[u][w]++;
        }
    }

    cout << endl;

    //create lp instance
    LPHelper lp{variable_names};
    //lp_solve pointer
    lprec *lp_solve = lp.get();

    //add rows
    lp.set_add_rowmode(TRUE);
    //todo: r != 0
    set_bounds(lp.get(), 1, 2, 6);
    add_flow_constraints(graph, lp, variable_names, edge_vars_x);
    add_bound_constraints(graph, lp, edge_vars_x, edge_vars_y);

    lp.set_add_rowmode(FALSE);


    //define objective
    lp.set_obj_fn({1}, {1});
    lp.set_minim();

    //you may use this for debugging
    //lp.write();

    //shows important messages while solving
    lp.set_verbose(IMPORTANT);

    //time to solve
    int res = lp.solve();
    if (res != OPTIMAL) { std::cerr << "No optimal solution found.\n"; }


    //print results
    std::cout << "Objective value: " << lp.get_objective() << "\n";
    auto vals = lp.get_solution();
    auto vars = lp.get_variable_names();
    for (int i = 0; i < vals.size(); i++) {
        std::cout << vars[i] << " : " << vals[i] << "\n";
    }

    return lp.get_objective();
}