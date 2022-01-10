package graphs;

/* Rozhranie pre reprezentaciu grafu o vrcholoch 0, 1, ..., n-1 pre nejake
   prirodzene cislo n: */
interface Graph {
    int getNumberOfVertices(); // Vrati pocet vrcholov grafu.
    int getNumberOfOutgoingEdges(int from);

    int getNumberOfEdges();    // Vrati pocet hran grafu.

    /* Prida hranu z vrcholu from do vrcholu to
       a vrati true, ak sa ju podarilo pridat: */
    boolean addEdge(int from, int to);

    /* Vrati true, ak existuje hrana z vrcholu from do vrcholu to: */
    boolean existsEdge(int from, int to);

    /* Vrati iterovatelnu skupinu pozostavajucu z prave vsetkych vrcholov,
       do ktorych vedie hrana z vrcholu vertex. Pre neorientovane grafy ide
       o prave vsetkych susedov vrcholu vertex: */
    Iterable<Integer> adjVertices(int vertex);
}
