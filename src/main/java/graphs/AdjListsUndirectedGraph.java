package graphs;

public class AdjListsUndirectedGraph extends AdjacencyListsGraph implements UndirectedGraph {
    private int numUndirectedEdges;

    public AdjListsUndirectedGraph(int numVertices) {
        super(numVertices);
        numUndirectedEdges = 0;
    }

    @Override
    public int getNumberOfEdges() {
        return numUndirectedEdges;
    }

    @Override
    public boolean addEdge(int from, int to) {
        boolean r = super.addEdge(from, to);
        super.addEdge(to, from);
        if (r) {
            numUndirectedEdges++;
        }
        return r;
    }
}
