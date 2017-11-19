package SalesMan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Regret {
    double regret;
    List<Node> nodes = new ArrayList<Node>();

    public Regret(){
        regret = 0;
    }

    public void addRegret(Double regret){
        this.regret +=regret;
    }
}
