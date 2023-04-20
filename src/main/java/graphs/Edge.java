package graphs;

public class Edge implements Comparable<Edge> {

    private final int from;
    private final int to;
    private int weight;

    public Edge(int from, int to, int weight) {
        if (from < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (to < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    public Edge(int from, int to) {
        if (from < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (to < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        this.from = from;
        this.to = to;
    }


    public int capacity() {
        return weight;
    }


    public int from() {
        return from;
    }


    public int to() {
       return to;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    public String toString() {
        return String.format("%d->%d %d", from, to, weight);
    }


}


