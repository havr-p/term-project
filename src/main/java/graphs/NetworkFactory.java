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

    public void addRandomEdges(FlowNetworkInterface flowNetwork, int from, Random random) {
        int source = flowNetwork.source();
        int sink = flowNetwork.sink();
        int maxCapacity = flowNetwork.maxCapacity();
        int numOfVertices = flowNetwork.getNumberOfVertices();
        if (from == source) {
            int edgesFromSourceCount = random.nextInt(1,numOfVertices - 1);
            for (int i = 0; i < edgesFromSourceCount; i++) {
                flowNetwork.addEdge(source, random.nextInt(1,numOfVertices - 1),
                        random.nextInt(maxCapacity));
            }
        }
        else if (from != sink) {

        }
    }

    public FlowNetworkInterface createRandomNetwork() {
        Random random = new Random();
        FlowNetworkInterface flowNetwork = createNetworkTemplate();

        //adding edges

        /*
        algorithm for adding edges:
        firstly we should add some edges from source
        how many? max numOfVertices-1

         */
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
