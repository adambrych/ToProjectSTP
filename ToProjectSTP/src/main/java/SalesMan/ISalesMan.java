package SalesMan;

import java.util.List;

public interface ISalesMan {
    void findPath(Node startNode);
    Node findBestNextNode(Node actualNode);
}
