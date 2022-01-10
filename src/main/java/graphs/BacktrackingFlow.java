package graphs;

import java.util.*;

/**
 * Trieda Flow reprezentuje celociselnu funkciu toku f: V x V -> Z, definovanu na mnozine hran E, pre ktoru plati:
 * 1. 0 =< f(v, u) <= capacity(v, u) pre ∀ (v, u) ∈ E
 * 2. ∑(f(u, w)) = 0, pre ∀ (u, w) ∈ E,  u != zdroj && u != ustie
 * 3. ∑(f(z, w)) = ∑(f(w, u)) = V; z = zdroj, u = ustie, V = velkost toku, ∀w ∈ V \ {z, u}
 * 4. f(u, v) = -f(v, u);
 * <p>
 * Nikde nenulovy k-tok je take ohodnotenie hran sieti, kde kazda hrana je hodnotena cislom zo Zk\{0}.
 * Algoritmus na najdenie nikde-nuloveho k-toku pomocou backtrackingu:
 * 1. Vzdy spustime funkciu na nejakom vrchole Zaciname hladat od zdroja.
 * Ku kazdej hrane, ktora zo zdroja vychadza, skusime pustit tok s hodnotou od 1 po k - 1.
 * Ak sme spustili funkciu zo zdroja, zapamatajme sucet hodnot, ktore sme priradili vychadzajucim hranam (∑(f(z, w))).
 * Kazdej vychadzajucej hrane priradime flow = 1, zapiseme do tabulky flow. Potom budeme skusat s vacsimi hodnotami (
 * todo: rozmysliet si nad tym, ako to presne urobit)
 * 2. Akonahle sme zavolali funkciu s ustim ako jej argumentom, ziadne dalsie volania z toho volania uz nespustime,
 * Skontrolujeme sucet tokov v hranach, ktore sa skoncili v usti, ak  ∑(f(z, w))out = ∑(f(w, u))in vratime zoznam flow
 * inac vratime null
 * 3. Ak nie sme v zdroje ani v usti, nech to je vrchol v, najdeme sucet hodnot na vchadzajucich hranach (w, v) a rozdelime ho
 * medzi hranami (v, u) - ak sa nepodari rozdelit (napr. do v vchadza iba 1 hrana s tokom 1, a vychadza uz 2 hrany), zahodime
 * cely vyrobeny zoznam flow
 */


public class BacktrackingFlow {
    private int MAX_FLOW_VALUE;
    private final List<Map<Integer, Integer>> flow = new ArrayList<>();
    private FlowNetworkInterface flowNetwork;
    private int sourceFlowSum;

    public BacktrackingFlow(FlowNetworkInterface flowNetwork, int sourceFlowSum, int MAX_FLOW_VALUE) {
        for (int i = 0; i < flowNetwork.getNumberOfVertices(); i++) {
            flow.add(new HashMap<>());
        }
        this.flowNetwork = flowNetwork;
        initializeFlow();
        this.sourceFlowSum = sourceFlowSum;
        this.MAX_FLOW_VALUE = MAX_FLOW_VALUE;
    }

    public boolean hasBridge(Graph graph) {
        return false;
    }

    public int getEdgeFlow(int from, int to) {
        return flow.get(from).get(to);
    }

    public void initializeFlow() {
        for (int from = 0; from < flowNetwork.getNumberOfVertices(); from++) {
            for (Integer to :
                    flowNetwork.adjVertices(from)) {
                setEdgeFlow(from, to, 0);
            }
        }
    }

    /**
     * sum of flows of edges pointing into vertex to
     */
    public int flowSumInVertex(int to) {
        int flowSum = 0;
        if (to == flowNetwork.source()) return sourceFlowSum;
        else {
            for (Map<Integer, Integer> edgeFlow :
                    flow) {
                if (edgeFlow.containsKey(to)) {
                    flowSum += edgeFlow.get(to);
                }
            }
        }
        return flowSum;
    }

    public boolean setEdgeFlow(int from, int to, int flowValue) {
        try {
            if (flowNetwork.existsEdge(from, to)) {
                if (flowValue <= flowNetwork.getCapacity(from, to)) {
                    flow.get(from).put(to, flowValue);
                    flow.get(to).put(from, -flowValue);
                    return true;
                } else {
                    throw new IllegalArgumentException("Wrong flow: flow cannot be greater than the capacity");
                }
            } else {
                throw new IllegalArgumentException("Edge does not exists");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return false;
        }
    }


    /**
     * @param from - vertex, from which we continue to compute required network flow
     */

    public void buildNowhere0Flows(int from, Stack<Integer> traversedVertices) {
      int inFlowSum = flowSumInVertex(from);
      int flowRemainder = inFlowSum;
      if (from == flowNetwork.sink()) {
          if (flowSumInVertex(from) == sourceFlowSum) {
              printFlow();
              System.out.println("It's flow!");
          }
      } else {
          List<Integer> adjVertices = new ArrayList<Integer>();
          flowNetwork.adjVertices(from).forEach(adjVertices::add);

          Map<Integer, Integer> capacityConstraints = new HashMap<>();
          for (Integer to:
               adjVertices) {
              capacityConstraints.put(to, flowNetwork.getCapacity(from, to));
          }

          OutgoingLabelingIterator iterator = new OutgoingLabelingIterator(MAX_FLOW_VALUE, inFlowSum, capacityConstraints);
          while (iterator.hasNext()) {
              flow.set(from, iterator.next());

              for (Integer to:
                   adjVertices) {
                  buildNowhere0Flows(to, null);
              }
          }
      }
    }

    void printFlow() {
        for (int from = 0; from < flowNetwork.getNumberOfVertices(); from++) {
            System.out.println("---" + from + "---");
            for (Integer to:
                    flowNetwork.adjVertices(from)) {
                System.out.println(from + " -> " + to + " : " + getEdgeFlow(from, to));
            }
        }
    }

    public static void main(String[] args) {
        /*FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(6, 4, 0, 5);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(0, 2, 4);
        flowNetwork.addEdge(1, 3, 4);
        flowNetwork.addEdge(2, 4, 4);
        flowNetwork.addEdge(3, 5, 4);
        flowNetwork.addEdge(4, 5, 4);

        BacktrackingFlow flow = new BacktrackingFlow(flowNetwork, 5, 4);

       // flow.printFlow();
        flow.buildNowhere0Flows(0);*/
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(3, 4, 0, 2);
        flowNetwork.addEdge(0, 1, 4);
        flowNetwork.addEdge(1, 2, 4);
        BacktrackingFlow flow1 = new BacktrackingFlow(flowNetwork, 4, 4);
        flow1.buildNowhere0Flows(0, null);

        //flow1.buildNowhere0Flows(0);
    }
}