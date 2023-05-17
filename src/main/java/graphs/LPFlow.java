package graphs;


import java.util.List;
public class LPFlow {
    List<List<Integer>> graph;
    public LPFlow(Graph graph) {
        this.graph = graph.adjacentLists();
    }




}
