package SalesMan;

import lombok.Data;

@Data
public class RegretFrom {
    Node from;
    Double profit;
    public RegretFrom( Node from, Double profit){
        this.from = from;
        this.profit = profit;
    }
}
