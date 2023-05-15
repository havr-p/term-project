package graphs;

public class Edge implements Comparable<Edge> {

    private final int from;
    private final int to;
    public Edge(int from, int to, int edgeIndex) {
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

    //@Override
   // public int compareTo(Edge that) {
     //   return Integer.compare(this.edgeIndex, that.edgeIndex);
    //}

    public String toString() {
        return String.format("%d->%d", from, to);
    }


}


