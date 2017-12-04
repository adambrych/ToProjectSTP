package SalesMan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tabu {
    int size;
    private List<ExtendingNode> tabuList;

    public Tabu(){
        size = 0;
        tabuList = new ArrayList<ExtendingNode>();
    }

    public Tabu(int size){
        this.size = size;
        this.tabuList = new ArrayList<ExtendingNode>();
    }

    public void addToList(ExtendingNode node){
        if(tabuList.size() < size)
            tabuList.add(node);
        else if(size > 0){
            tabuList.remove(0);
            tabuList.add(node);
        }
    }

    public boolean isNodeInList(Node node){
        for(ExtendingNode extendingNode : tabuList){
            if(extendingNode.getOperationType() == OperationType.ADD){
                Node from = extendingNode.getFrom();
                Node to = extendingNode.getTo();
                Node last = from.getNext();
                if(from.getIndex() == node.getIndex() || to.getIndex() == node.getIndex() || last.getIndex() == node.getIndex())
                    return true;
            }
            else if(extendingNode.getOperationType() == OperationType.REMOVE){
                Node nodeToRemove = extendingNode.getTo();
                Node prevNode = nodeToRemove.getPrev();
                Node nextNode = nodeToRemove.getNext();
                if(nodeToRemove.getIndex() == node.getIndex() || prevNode.getIndex() == node.getIndex() || nextNode.getIndex() == node.getIndex())
                    return true;
            }
            else{
                Node firstNode = extendingNode.getFrom();
                Node secondNode = firstNode.getNext();
                Node thirdNode = extendingNode.getTo();
                Node fourthNode = thirdNode.getNext();
                if(firstNode.getIndex() == node.getIndex() || secondNode.getIndex() == node.getIndex() || thirdNode.getIndex() == node.getIndex() || fourthNode.getIndex() == node.getIndex())
                    return true;
            }
        }
        return false;
    }
}
