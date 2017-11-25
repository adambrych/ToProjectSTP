package SalesMan;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalesManRandom extends SalesMan {

    protected String methodName = "Random";
    private Random randomGenerator;

    public SalesManRandom(List<Node> nodes) {
        super(nodes);
    }

    private void init(Node startNode){
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
            path.add(nextNode);
            notVisitedNodes.remove(nextNode);
            actualNode = nextNode;
        }
        if (path.size() > 1) {
            path.add(startNode);
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
        int index = randomGenerator.nextInt(notVisitedNodes.size());
        Node bestNode = notVisitedNodes.get(index);
        double bestProfit = getProfit(actualNode, bestNode);
        if (bestProfit > 0) {
            profit += bestProfit;
            return bestNode;
        }
        return null;
    }

    @Override
    public double getProfit(Node from, Node to) {
        return super.getProfit(from, to);
    }
}
