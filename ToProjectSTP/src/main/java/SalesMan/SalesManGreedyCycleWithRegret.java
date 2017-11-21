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
        if(bestRegret == null || bestRegret.getTo() == null)
            return null;
        if(bestRegret != null && bestRegret.getBestFrom() != null && getProfit(bestRegret.getBestFrom(), bestRegret.getTo()) >= 0) {
            bestNode = bestRegret.getTo();
            if(visitedNodes.size() > 2)
                super.changeCycle(bestRegret.getBestFrom(), bestRegret.getTo());
            }
        return bestNode;
    }

    private Regret findBestRegret(){
        if(visitedNodes.size() > 2) {
            List<Regret> regrets = new ArrayList<Regret>();
            for (Node to : notVisitedNodes) {
                Regret regret = new Regret(to);
                for (Node from : visitedNodes) {
                    regret.getRegrets().add(new RegretFrom(from, super.getProfit(from, to)));
                }
                regret.countRegret();
                regrets.add(regret);
            }
            /*double best1 = -10000000;
            double best2 = -10000000;
            Regret bestRegret1 = null;
            Regret bestRegret2 = null;
            for (Regret regret : regrets) {
                if (regret.getRegrets().get(0).getProfit() > best1) {
                    best2 = best1;
                    best1 = regret.getRegrets().get(0).getProfit();
                    bestRegret2 = bestRegret1;
                    bestRegret1 = regret;
                }
                else if(regret.getRegrets().get(0).getProfit() > best2){
                    best2 = regret.getRegrets().get(0).getProfit();
                    bestRegret2 = regret;
                }
            }
            if(best2 < 0 || bestRegret1.getRegret() >= bestRegret2.getRegret())
                return bestRegret1;
            else
                return bestRegret2;*/
            List<Regret> regrets2 = new ArrayList<Regret>();
            for(Regret regret : regrets){
                if(regret.getRegrets().get(0).getProfit() >= 0)
                    regrets2.add(regret);
            }
            double best = -10000000;
            Regret bestRegret = null;
            for(Regret regret : regrets2){
                if(regret.getRegret() > best){
                    best = regret.getRegret();
                    bestRegret = regret;
                }

            }
            return bestRegret;

        }
        else{
            Regret regret = new Regret(super.findBestNextNode(null, notVisitedNodes));
            regret.setBestFrom(visitedNodes.get(0));
            return regret;
        }
    }

}
