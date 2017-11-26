package SalesMan;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalesManRandom extends SalesMan {

    protected String methodName = "Random";
    private Random randomGenerator;
    private int nodesCount;

    public SalesManRandom(List<Node> nodes) {
        super(nodes);
        randomGenerator = new Random();
    }

    private void init(Node startNode){
        visitedNodes = new ArrayList<Node>();
        visitedNodes.add(startNode);
        path = new ArrayList<Node>();
        path.add(startNode);
        profit = 0;
        nodesCount = randomGenerator.nextInt(nodes.size());
        if (nodesCount < 4) {
            nodesCount = 50;
        }
    }

    @Override
    public void findPath(Node startNode) {
        init(startNode);
        notVisitedNodes = new ArrayList<Node>(nodes);
        Node actualNode = new Node(startNode);
        notVisitedNodes.remove(startNode);

        for(int step = 0; step<nodesCount; step++){
            int index = randomGenerator.nextInt(notVisitedNodes.size());
            Node nextNode = notVisitedNodes.get(index);
            if (step == 0) {
                startNode.setNext(nextNode);
                nextNode.setPrev(startNode);
            } else {
                actualNode.setNext(nextNode);
                nextNode.setPrev(actualNode);
            }
            path.add(nextNode);
            notVisitedNodes.remove(nextNode);
            visitedNodes.add(nextNode);
            actualNode = nextNode;
        }
        path.add(startNode);
        actualNode.setNext(startNode);
        startNode.setPrev(actualNode);
        profit += getProfit(actualNode, startNode);
        writeToFile(profit, methodName);
        if(profit>bestProfit){
            bestProfit = profit;
            bestPath = path;
        }
    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes) {
        throw new NotImplementedException();
    }

    @Override
    public double getProfit(Node from, Node to) {
        return super.getProfit(from, to);
    }
}
