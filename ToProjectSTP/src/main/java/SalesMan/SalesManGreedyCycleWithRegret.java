package SalesMan;


import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SalesManGreedyCycleWithRegret extends SalesManGreedyCycle{

    private double actualBestRegret = - Double.MAX_VALUE;

    public SalesManGreedyCycleWithRegret(List<Node> nodes){
        super(nodes);
        super.methodName = "GreedyCycleWithRegret";
    }

    @Override
    public void findPath(Node startNode){
        super.init(startNode); // TODO: delete
        super.findPath(startNode);
    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> visitedNodes) {
        ArrayList<Double> regrets = new ArrayList<Double>();
        Node bestNode = null;
        double bestProfit = 0;
        for (Node node : visitedNodes) {
            double profit = super.getProfit(node, actualNode);
            regrets.add(profit);
            if (profit > bestProfit) {
                bestProfit = profit;
                bestNode = node;
            }
        }
        Collections.sort(regrets);
        Collections.reverse(regrets);
        if (regrets.size() > 1 && regrets.get(0) > 0) {
            double regret = regrets.get(0) - regrets.get(1);
            if (regret > actualBestRegret) {
                actualBestRegret = regret;
                return bestNode;
            }
            return null;
        }
        return null;
    }

    @Override
    public Node findBestNextNode(List<Node> notVisitedNodes) {
        Node bestNodeFrom = null;
        Node bestNode = null;
        if (visitedNodes.size() >= 3) {
            for (Node node : notVisitedNodes) {
                Node tempBestNode = findBestNextNode(node, visitedNodes);
                if (tempBestNode != null) {
                    bestNodeFrom = tempBestNode;
                    bestNode = node;
                }
            }
        } else {
            double bestProfit = 0;
            for (Node node : visitedNodes) {
                Node findNode = super.findBestNextNode(node, notVisitedNodes);
                if (findNode != null) {
                    double profit = getProfit(node, findNode);
                    if (profit >= bestProfit) {
                        bestNodeFrom = node;
                        bestProfit = profit;
                        bestNode = findNode;
                    }
                }
            }
        }

        if (bestNode != null)
            changeCycle(bestNodeFrom, bestNode);
        actualBestRegret = 0;
        return bestNode;
    }


}
