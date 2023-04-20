package graphs;

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
        FlowNetworkInterface flowNetworkInterface = new FlowNetwork(numOfVertices, maxCapacity);
        return flowNetworkInterface;
    }


    public FlowNetworkInterface createRandomNetwork() {
        return null;
    }

    public FlowNetworkInterface createNetworkFromInput() {

        FlowNetworkInterface flowNetworkInterface = createNetworkTemplate();
        Scanner in = new Scanner(System.in);
        System.out.println("Input edges:");
        for (int i = 0; i < flowNetworkInterface.getNumberOfEdges(); i++) {
            int from = in.nextInt();
            int to = in.nextInt();
            int capacity = in.nextInt();
            flowNetworkInterface.addEdge(from, to, capacity);
        }
        return flowNetworkInterface;
    }
}
