package SalesMan;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalesManRandom extends SalesMan {

    protected String methodName = "Random";
    private Random randomGenerator;

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
    }

    @Override
    public void findPath(Node startNode) {
        init(startNode);
        notVisitedNodes = new ArrayList<Node>(nodes);
        Node actualNode = new Node(startNode);
        notVisitedNodes.remove(startNode);

        for(int step = 0; step<nodes.size()-1; step++){
            Node nextNode = findBestNextNode(actualNode, notVisitedNodes);
            if(nextNode == null)
                break;
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
        if (path.size() > 1) {
            path.add(startNode);
            actualNode.setNext(startNode);
            startNode.setPrev(actualNode);
            profit += getProfit(actualNode, startNode);
        } else {
            profit += startNode.getProfit();
        }
        writeToFile(profit, methodName);
        if(profit>bestProfit){
            bestProfit = profit;
            bestPath = path;
        }
    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes) {
        for (int i = 0; i < notVisitedNodes.size(); ++i) {
            int index = randomGenerator.nextInt(notVisitedNodes.size());
            Node bestNode = notVisitedNodes.get(index);
            double bestProfit = getProfit(actualNode, bestNode);
            if (bestProfit > 0) {
                profit += bestProfit;
                bestNode.setPrev(actualNode);
                actualNode.setNext(bestNode);
                return bestNode;
            }
        }
        return null;
    }

    @Override
    public double getProfit(Node from, Node to) {
        return super.getProfit(from, to);
    }
}
