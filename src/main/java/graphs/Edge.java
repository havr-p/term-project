package graphs;


public class Edge implements Comparable<Edge> {
    private final int from;
    private final int to;

    public Edge(int from, int to) {
        if (from < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (to < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        this.from = from;
        this.to = to;
    }


    public int from() {
        return from;
    }


    public int to() {
        return to;
    }

    @Override
    public int compareTo(Edge that) {
        return Integer.compare(this.from, that.from);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != edge.from) return false;
        return to == edge.to;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }

    public String toString() {
        return String.format("%d->%d", from, to);
    }


}


