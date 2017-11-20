package SalesMan;

import lombok.Data;

import java.util.*;

@Data
public class Regret {
    private Node to;
    private List<RegretFrom> regrets = new ArrayList<RegretFrom>();
    private Node bestFrom;

    private double regret;
    public Regret(Node to){
        regret = 0;
        this.to = to;
    }

    public void countRegret(){
        regrets.sort(new Comparator<RegretFrom>() {
            @Override
            public int compare(RegretFrom o1, RegretFrom o2) {
                return o1.getProfit().compareTo(o1.getProfit());
            }
        });
        bestFrom = regrets.get(0).getFrom();
        regret = regrets.get(0).getProfit() - regrets.get(1).getProfit();
    }
}
