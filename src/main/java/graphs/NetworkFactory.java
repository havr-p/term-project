package graphs;

import java.util.Scanner;

public class NetworkFactory {

    public FlowNetwork createNetworkTemplate() {
        Scanner in = new Scanner(System.in);
        System.out.println("Set number of vertices:");
        int numOfVertices = in.nextInt();
        System.out.println("Set number of edges:");
        int numOfEdges = in.nextInt();
        System.out.println("Set maximal capacity:");
        int maxCapacity = in.nextInt();
        System.out.println("Set the source and the sink:");
        FlowNetwork flowNetwork = new AdjListsFlowNetwork(numOfVertices, maxCapacity);
        return flowNetwork;
    }


    public FlowNetwork createRandomNetwork() {
        return null;
    }

    public FlowNetwork createNetworkFromInput() {

        FlowNetwork flowNetwork = createNetworkTemplate();
        Scanner in = new Scanner(System.in);
        System.out.println("Input edges:");
        for (int i = 0; i < flowNetwork.getNumberOfEdges(); i++) {
            int from = in.nextInt();
            int to = in.nextInt();
            int capacity = in.nextInt();
            flowNetwork.addEdge(from, to, capacity);
        }
        return flowNetwork;
    }
}
