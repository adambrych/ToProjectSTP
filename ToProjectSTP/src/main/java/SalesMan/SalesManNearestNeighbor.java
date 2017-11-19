/*
Iteracyjne dodawanie do trasy najbliżej leżącego miasta.
 */
package SalesMan;
import java.util.ArrayList;
import java.util.List;

public class SalesManNearestNeighbor extends SalesMan {

    private static final String methodName = "NearestNeighbor";
    public SalesManNearestNeighbor(List<Node> nodes){
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
        path.add(startNode);
        profit += getCost(actualNode.getIndex(), startNode.getIndex()) + startNode.getProfit();
        writeToFile(profit, methodName);
        if(profit>bestProfit){
            bestProfit = profit;
            bestPath = path;
        }
    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes) {
        Node bestNode = super.findBestNextNode(actualNode, notVisitedNodes);
        if(bestNode != null) {
            Double bestProfit = getCost(actualNode.getIndex(), bestNode.getIndex()) + bestNode.getProfit();
            if (bestProfit > 0)
                profit += bestProfit;
        }
        return bestNode;
    }
}
