package SalesMan;


import lombok.Getter;

@Getter
public class ExtendingNode {
    private Node from;
    private Node to;
    private double profit;
    private OperationType operationType;

    public ExtendingNode(Node from, Node to, double profit, OperationType operationType) {
        this.from = from;
        this.to = to;
        this.profit = profit;
        this.operationType = operationType;
    }
}
