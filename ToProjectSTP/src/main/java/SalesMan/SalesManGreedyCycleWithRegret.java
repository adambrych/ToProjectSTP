package SalesMan;


import java.util.ArrayList;
import java.util.List;

public class SalesManGreedyCycleWithRegret extends SalesManGreedyCycle{

    private static final int regretSteps = 5;

    public SalesManGreedyCycleWithRegret(List<Node> nodes){
        super(nodes);
        super.methodName = "GreedyCycleWithRegret";
    }

    @Override
    public void findPath(Node startNode){
        super.init(startNode);
        super.findPath(startNode);
    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes) {
        Node bestNode = null;
        Node from = null;
        double bestRegret = 0;
        List<Node> visited = new ArrayList<Node>(visitedNodes);
        for(Node node : visited) {
            Regret regret = countRegret(node);
            if(regret != null && regret.getRegret() > bestRegret){
                from = getNode(node);
                bestRegret = regret.getRegret();
                bestNode = getNode(regret.getNodes().get(0));
            }
        }
        if(bestNode != null)
            super.changeCycle(from, bestNode);
        return bestNode;
    }

    private Regret countRegret(Node actualNode){
        Regret regret = new Regret();
        List<Node> beforeNotVisited = cloneList(notVisitedNodes);
        Node prev = actualNode;
        for(int i =0; i< Math.min(regretSteps, notVisitedNodes.size()); i++) {
            Node actual = super.findBestNextNode(prev, beforeNotVisited);
            if(actual != null){
                regret.addRegret(getCost(prev.getIndex(), actual.getIndex()) + actual.getProfit());
                regret.getNodes().add(actual);
                beforeNotVisited.remove(actual);
            }
            else{
                break;
            }
            prev = actual;
        }
        return regret;
    }

    private List<Node> cloneList(List<Node> nodes){
        List<Node> newList = new ArrayList<Node>();
        for(Node node : nodes)
            newList.add(new Node(node));
        return newList;
    }

    private Node getNode(Node node){
      for(Node actualNode : notVisitedNodes){
          if(actualNode.getIndex() == node.getIndex())
              return actualNode;
      }
        for(Node actualNode : visitedNodes){
            if(actualNode.getIndex() == node.getIndex())
                return actualNode;
        }
      return null;
    }

}
