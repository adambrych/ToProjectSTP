package SalesMan;

import lombok.Data;

@Data
public class Node {
    int index;
    double x;
    double y;
    double profit;
    private Node prev;
    private Node next;

    public Node(int index, double x, double y, double profit){
        this.index = index - 1;
        this.x = x;
        this.y = y;
        this.profit = profit;
        this.next = null;
        this.prev = null;
    }

    public Node(Node node){
        this.index = node.index;
        this.x = node.x;
        this.y = node.y;
        this.profit = node.profit;
        this.prev = node.prev;
        this.next = node.next;
    }
}
