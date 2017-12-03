package SalesMan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tabu {
    int size;
    private List<Node> tabuList;

    public Tabu(int size){
        this.size = size;
        this.tabuList = new ArrayList<Node>();
    }

    public void addToList(Node node){
        if(tabuList.size() < size)
            tabuList.add(node);
        else{
            tabuList.remove(0);
            tabuList.add(node);
        }
    }

    public boolean isNodeInList(Node node){
        return tabuList.contains(node);
    }
}
