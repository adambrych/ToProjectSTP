package SalesMan;

import java.util.ArrayList;
import java.util.HashMap;

public class SalesManNeighbourhood {

    private SalesMan salesMan;
    private SalesManGreedyCycle salesManGreedyCycle;

    public SalesManNeighbourhood(SalesMan salesMan) {
        this.salesMan = salesMan;
        salesManGreedyCycle = new SalesManGreedyCycle(salesMan);
    }

    private ExtendingNode addNewNode() {
        return salesManGreedyCycle.findNodeToExtend(salesMan.getNotVisitedNodes(), salesMan.getTabu());
    }

    private ExtendingNode removeNode() {
        Node bestNodeToRemove = null;
        double bestCost = -Double.MAX_VALUE;
        if (salesMan.getVisitedNodes().size() < 3) {
            return new ExtendingNode(null, null, 0, OperationType.REMOVE);
        }
        for (Node node : salesMan.getVisitedNodes()) {
            if(salesMan.getTabu().isNodeInList(node))
                continue;
            double cost = 0;
            double actualCost = 0;
            actualCost = salesMan.getCost(node.getPrev().getIndex(), node.getIndex())
                    + node.getProfit() + salesMan.getCost(node.getIndex(), node.getNext().getIndex());
            cost = salesMan.getCost(node.getPrev().getIndex(), node.getNext().getIndex());
            double profit = Math.abs(actualCost) - Math.abs(cost);
            if (actualCost < 0 && cost > actualCost && profit > bestCost) {
                bestCost = profit;
                bestNodeToRemove = node;
            }
        }
        if (bestCost != -Double.MAX_VALUE) {
            bestCost = Math.abs(bestCost);
        }
        return new ExtendingNode(null, bestNodeToRemove, bestCost, OperationType.REMOVE);
    }

    private ExtendingNode swapEdges() {
        Node[] bestNodes = new Node[2];
        double bestCost = -Double.MAX_VALUE;
        if (salesMan.getVisitedNodes().size() < 4) {
            return new ExtendingNode(null, null, 0, OperationType.SWAP);
        }
        for (Node firstNode : salesMan.getVisitedNodes()) {
            for (Node thirdNode : salesMan.getVisitedNodes()) {
                if (thirdNode != firstNode.getNext() && thirdNode != firstNode.getPrev() && thirdNode != firstNode) {
                    Node secondNode = firstNode.getNext();
                    Node fourthNode = thirdNode.getNext();
                    if(salesMan.getTabu().isNodeInList(firstNode) || salesMan.getTabu().isNodeInList(secondNode) || salesMan.getTabu().isNodeInList(thirdNode) || salesMan.getTabu().isNodeInList(fourthNode))
                        continue;
                    double actualCost = salesMan.getCost(firstNode.getIndex(), secondNode.getIndex())
                            + salesMan.getCost(thirdNode.getIndex(), fourthNode.getIndex());
                    double cost = salesMan.getCost(firstNode.getIndex(), thirdNode.getIndex())
                            + salesMan.getCost(secondNode.getIndex(), fourthNode.getIndex());
                    double profit = Math.abs(actualCost) - Math.abs(cost);
                    if (actualCost < 0 && cost > actualCost && profit > bestCost) {
                        bestCost = profit;
                        bestNodes[0] = firstNode;
                        bestNodes[1] = thirdNode;
                    }
                }
            }
        }
        if (bestCost != -Double.MAX_VALUE) {
            bestCost = Math.abs(bestCost);
        }
        return new ExtendingNode(bestNodes[0], bestNodes[1], bestCost, OperationType.SWAP);
    }

    public SalesMan extendCycle() {
        double profit;
        double profitAmount = 0;
        double actualCycleProfit =  salesMan.getProfit();
        ExtendingNode bestResult;
        long startTime = System.currentTimeMillis();
        long elapsedTime=0;
        do {
            profit = -100000000;
            bestResult = null;
            ArrayList<ExtendingNode> results = new ArrayList<ExtendingNode>();
            results.add(addNewNode());
            results.add(removeNode());
            results.add(swapEdges());
            for (ExtendingNode result : results) {
                if (checkProfit(result, profit)) {
                    profit = result.getProfit();
                    bestResult = result;
                }
            }
            if (bestResult != null) {
                profitAmount += bestResult.getProfit();
                switch (bestResult.getOperationType()) {
                    case ADD:
                        addNodeToCycle(bestResult);
                        break;
                    case REMOVE:
                        removeNodeFromCycle(bestResult);
                        break;
                    case SWAP:
                        swapEdgesInCycle(bestResult);
                        break;
                }
                salesMan.getTabu().addToList(bestResult);

            }
            long stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
        } while (elapsedTime<1076);
        salesMan.setActualProfit(profitAmount + actualCycleProfit);
        salesMan.preparePath();
        salesMan.writeToFile(profitAmount + actualCycleProfit, salesMan.getClass().toString());
        //System.out.println(salesMan.getClass().toString() + " " +  profitAmount + " " + Double.toString(profitAmount + actualCycleProfit));
        return salesMan;
    }

    private boolean checkProfit(ExtendingNode result, double profit) {
        return (result.getProfit() > profit);
    }

    private void addNodeToCycle(ExtendingNode node) {
        Node from = node.getFrom();
        Node to = node.getTo();
        Node last = from.getNext();
        from.setNext(to);
        to.setNext(last);
        to.setPrev(from);
        last.setPrev(to);
        salesMan.setProfit(salesMan.getProfit() + node.getProfit());
        salesMan.getVisitedNodes().add(to);
        salesMan.getNotVisitedNodes().remove(to);
    }

    private void removeNodeFromCycle(ExtendingNode node) {
        Node nodeToRemove = node.getTo();
        Node prevNode = nodeToRemove.getPrev();
        Node nextNode = nodeToRemove.getNext();
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        salesMan.setProfit(salesMan.getProfit() + node.getProfit());
        salesMan.getVisitedNodes().remove(nodeToRemove);
        salesMan.getNotVisitedNodes().add(nodeToRemove);
    }

    private void swapEdgesInCycle(ExtendingNode node) {
        Node firstNode = node.getFrom();
        Node secondNode = firstNode.getNext();
        Node thirdNode = node.getTo();
        Node fourthNode = thirdNode.getNext();
        firstNode.setNext(thirdNode);
        Node tempNode = thirdNode.getPrev();
        thirdNode.setPrev(firstNode);
        thirdNode.setNext(tempNode);
        tempNode = secondNode.getNext();
        secondNode.setNext(fourthNode);
        secondNode.setPrev(tempNode);
        fourthNode.setPrev(secondNode);
        while (tempNode != thirdNode) {
            Node temp = tempNode.getNext();
            tempNode.setNext(tempNode.getPrev());
            tempNode.setPrev(temp);
            tempNode = temp;
        }
        salesMan.setProfit(salesMan.getProfit() + node.getProfit());
    }

}
