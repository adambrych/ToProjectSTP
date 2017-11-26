package SalesMan;

import java.util.ArrayList;

public class SalesManNeighbourhood {

    private SalesMan salesMan;
    private SalesManGreedyCycle salesManGreedyCycle;

    public SalesManNeighbourhood(SalesMan salesMan) {
        this.salesMan = salesMan;
        salesManGreedyCycle = new SalesManGreedyCycle(salesMan);
    }

    private ExtendingNode addNewNode() {
        return salesManGreedyCycle.findNodeToExtend(salesMan.getNotVisitedNodes());
    }

    private ExtendingNode removeNode() {
        Node bestNodeToRemove = null;
        double bestCost = -Double.MAX_VALUE;
        if (salesMan.getVisitedNodes().size() < 3) {
            return new ExtendingNode(null, null, 0, OperationType.REMOVE);
        }
        for (Node node : salesMan.getVisitedNodes()) {
            double cost = 0;
            double actualCost = 0;
            actualCost = salesMan.getCost(node.getPrev().getIndex(), node.getIndex())
                    + node.getProfit() + salesMan.getCost(node.getIndex(), node.getNext().getIndex());
            cost = salesMan.getCost(node.getPrev().getIndex(), node.getNext().getIndex());
            if (actualCost < 0 && cost < actualCost && cost < bestCost) {
                System.out.println("TAAAAAK");
                bestCost = cost;
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
                    double actualCost = salesMan.getCost(firstNode.getIndex(), secondNode.getIndex())
                            + salesMan.getCost(thirdNode.getIndex(), fourthNode.getIndex());
                    double cost = salesMan.getCost(firstNode.getIndex(), thirdNode.getIndex())
                            + salesMan.getCost(secondNode.getIndex(), fourthNode.getIndex());
//                    System.out.println(actualCost);
//                    System.out.println(cost);
//                    System.out.println(firstNode.getIndex() + " " + thirdNode.getIndex());
//                    System.out.println("-------------");
                    if (cost < actualCost && cost < bestCost) {
                        System.out.println("TAL TAL TLA");
                        bestCost = cost;
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

    public void extendCycle() {
        double profit;
        double profitAmount = 0;
        ExtendingNode bestResult;
        do {
            profit = 0;
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
//                System.out.println(salesMan.getNotVisitedNodes().size());
//                System.out.println(bestResult.getOperationType());
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
            }
        } while (profit > 0);
        System.out.println("--------");
//        System.out.println(profitAmount);
    }

    private boolean checkProfit(ExtendingNode result, double profit) {
        return (result.getProfit() > 0 && result.getProfit() > profit);
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
        nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
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
        thirdNode.setPrev(firstNode);
        Node tempNode = secondNode.getNext();
        secondNode.setNext(fourthNode);
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
