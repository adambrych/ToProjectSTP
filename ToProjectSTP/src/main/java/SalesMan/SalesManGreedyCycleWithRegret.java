package SalesMan;


import java.util.ArrayList;
import java.util.List;

public class SalesManGreedyCycleWithRegret extends SalesManGreedyCycle{

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
        Regret bestRegret = findBestRegret();
        if(bestRegret != null && bestRegret.getBestFrom() != null && getProfit(bestRegret.getBestFrom(), bestRegret.getTo()) >= 0) {
            bestNode = bestRegret.getTo();
            super.changeCycle(bestRegret.getBestFrom(), bestRegret.getTo());
            }
        return bestNode;
    }

    private Regret findBestRegret(){
        if(visitedNodes.size() > 1) {
            List<Regret> regrets = new ArrayList<Regret>();
            for (Node to : notVisitedNodes) {
                Regret regret = new Regret(to);
                for (Node from : visitedNodes) {
                    regret.getRegrets().add(new RegretFrom(from, super.getProfit(from, to)));
                }
                regret.countRegret();
                regrets.add(regret);
            }
            double bestRegret = -1000000000;
            Regret best = null;
            for (Regret regret : regrets) {
                if (regret.getRegret() > bestRegret) {
                    bestRegret = regret.getRegret();
                    best = regret;
                }
            }
            return best;
        }
        else{
            Regret regret = new Regret(super.findBestNextNode(null, notVisitedNodes));
            regret.setBestFrom(visitedNodes.get(0));
            return regret;
        }
    }

}
