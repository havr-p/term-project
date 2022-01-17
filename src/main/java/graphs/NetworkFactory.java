package graphs;

import java.util.Random;
import java.util.Scanner;

public class NetworkFactory {

    public FlowNetworkInterface createNetworkTemplate() {
        Scanner in = new Scanner(System.in);
        System.out.println("Set number of vertices:");
        int numOfVertices = in.nextInt();
        System.out.println("Set number of edges:");
        int numOfEdges = in.nextInt();
        System.out.println("Set maximal capacity:");
        int maxCapacity = in.nextInt();
        System.out.println("Set the source and the sink:");
        int source = in.nextInt();
        int sink = in.nextInt();
        FlowNetworkInterface flowNetwork = new AdjListsFlowNetwork(numOfVertices, maxCapacity, source, sink);
        return flowNetwork;
    }


    public FlowNetworkInterface createRandomNetwork() {
        return null;
    }

    public FlowNetworkInterface createNetworkFromInput() {

        FlowNetworkInterface flowNetwork = createNetworkTemplate();
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
