/*
Trasa jest budowana tak, że zawsze tworzy cykl
Hamiltona. W każdej iteracji dodawany jest jeden
najkrótszy łuk z pozostałych dostępnych. Czyli w naszym przypadku dodawane miasto z największym zyskiem.
 */

package SalesMan;


import java.util.ArrayList;
import java.util.List;

public class SalesManGreedyCycle extends SalesMan {

    protected String methodName = "Greedy Cycle";

    public SalesManGreedyCycle(List<Node> nodes){
        super(nodes);

    }

    protected void init(Node startNode){
        clearPrevNext();
        visitedNodes = new ArrayList<Node>();
        visitedNodes.add(startNode);
        path = new ArrayList<Node>();
        profit = 0;

    }

    @Override
    public void findPath(Node startNode) {
        init(startNode);
        notVisitedNodes = new ArrayList(nodes);
        notVisitedNodes.remove(startNode);

        for(int step = 0; step<nodes.size()-1; step++){
            Node nextNode = findBestNextNode(null);
            if(nextNode == null)
                break;
            notVisitedNodes.remove(nextNode);
            visitedNodes.add(nextNode);
        }

        preparePath();
        writeToFile(profit, methodName);
    }

    protected Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes){
        return super.findBestNextNode(actualNode, notVisitedNodes);
    }

    @Override
    public Node findBestNextNode(Node actualNode) {
        Node bestNode = null;
        Node from = null;
        double bestProfit = 0;

        for(Node node : visitedNodes) {
            Node findNode = super.findBestNextNode(node);
            if(findNode != null) {
                double profit = getCost(node.getIndex(), findNode.getIndex()) + findNode.getProfit();
                if (profit  >= bestProfit) {
                    from = node;
                    bestProfit = profit;
                    bestNode = findNode;
                }
            }
        }
        if(bestNode != null)
            changeCycle(from, bestNode);
        return bestNode;
    }

    protected void changeCycle(Node from, Node to){
        if(visitedNodes.size() == 1){
            from.setNext(to);
            to.setPrev(from);
        }
        else if(visitedNodes.size() == 2){
            if(from.getPrev()!= null) {
                from.setNext(to);
                from.getPrev().setPrev(to);
                to.setNext(from.getPrev());
                to.setPrev(from);
            }
            else{
                from.getNext().setNext(from);
                from.getNext().setPrev(to);
                to.setPrev(from);
                to.setNext(from.getNext());
                from.setPrev(from.getNext());
                from.setNext(to);
            }
        }
        else{
            Node lastNext = from.getNext();
            from.setNext(to);
            to.setNext(lastNext);
            to.setPrev(from);
            lastNext.setPrev(to);
        }
    }

    private void preparePath(){
        Node node = visitedNodes.get(0);
        if(visitedNodes.size() > 1) {
            do {
                path.add(node);
                profit += getCost(node.getIndex(), node.getNext().getIndex()) + node.getNext().getProfit();
                node = node.getNext();
            } while (node != visitedNodes.get(0));
            profit += getCost(visitedNodes.get(0).getPrev().getIndex(), visitedNodes.get(0).getIndex()) + visitedNodes.get(0).getProfit();
        }
        else
            profit +=node.getProfit();
        path.add(visitedNodes.get(0));
    }

    private void clearPrevNext(){
        for(Node node : nodes){
            node.setPrev(null);
            node.setNext(null);
        }
    }
}
