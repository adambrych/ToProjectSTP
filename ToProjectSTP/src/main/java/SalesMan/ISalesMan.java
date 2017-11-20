package SalesMan;

import java.util.List;

public interface ISalesMan {
    void findPath(Node startNode);
    Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes);
    double getProfit(Node from, Node to);
}
