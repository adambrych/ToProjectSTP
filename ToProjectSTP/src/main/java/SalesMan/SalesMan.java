package SalesMan;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class SalesMan implements ISalesMan {
    protected String methodName = "";
    private static final int cost = 6;
    public static final String fileNameToWrite = "result.txt";
    protected List<Node> nodes;
    protected double[][] distances;
    protected List<Node> path = new ArrayList<Node>();
    protected double profit = 0;
    List<Node> visitedNodes;
    List<Node> notVisitedNodes;
    protected double bestProfit = 0;
    private double average = 0;
    List<Node> bestPath = new ArrayList<Node>();
    private double actualProfit = 0;

    public SalesMan(List<Node> nodes){
        this.nodes = nodes;
        this.distances = new double[nodes.size()][nodes.size()];
        findDistancesBetweenNodes();
    }

    private void findDistancesBetweenNodes(){
        for(Node from : nodes)
            for(Node to : nodes)
                countDistance(from, to);
    }

    private void countDistance(Node from, Node to){
        if(from.getIndex() != to.getIndex()){
            double distance = Math.sqrt(Math.pow(from.getX() - to.getX(),2D) + Math.pow(from.getY() - to.getY(), 2D));
            distances[from.getIndex()][to.getIndex()] = distance;
            distances[to.getIndex()][from.getIndex()] = distance;
        }
        else{
            distances[from.getIndex()][to.getIndex()] = 0;
        }
    }

    public double getCost(int from, int to){
        return -1 * distances[from][to] * cost;
    }

    @Override
    public void findPath(Node startNode) {

    }

    @Override
    public Node findBestNextNode(Node actualNode, List<Node> notVisitedNodes) {
        double bestProfit = 0;
        Node bestNode = null;
        for (Node node : notVisitedNodes) {
            double profit = getProfit(actualNode, node);
            if (profit >= bestProfit) {
                bestProfit = profit;
                bestNode = node;
            }
        }

        return bestNode;
    }

    @Override
    public double getProfit(Node from, Node to) {
        double profit = getCost(from.getIndex(), to.getIndex());
        profit += to.profit;
        return profit;
    }

    protected void writeToFile(double profit, String methodName){
        try {
            Path file = Paths.get(fileNameToWrite);
            if(methodName.startsWith("class"))
                methodName = methodName.substring(6);
            String line = prepareLineToWrite(path, profit, methodName);
            average += profit;
            List<String> lines = Arrays.asList(line);
            Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prepareLineToWrite(List<Node> path, double profit, String methodName){
        StringBuilder line = new StringBuilder();
        line.append(methodName);
        line.append(' ');
        line.append("profit ");
        line.append(profit);
        line.append(' ');
        for(Node node : path){
            line.append(Integer.toString(node.getIndex()));
            line.append(' ');
        }
        return line.toString();
    }

    public void preparePath(){
        Node node = visitedNodes.get(0);
        path = new ArrayList<Node>();
        if(visitedNodes.size() > 1) {
            do {
                path.add(node);
                profit += getCost(node.getIndex(), node.getNext().getIndex()) + node.getNext().getProfit();
                node = node.getNext();
            } while (node != visitedNodes.get(0));
        }
        else
            profit +=node.getProfit();
        path.add(visitedNodes.get(0));
    }

    public void writeBestResult(){
        try {
            Path file = Paths.get(fileNameToWrite);
            Files.write(file, Arrays.asList("BEST: " + Double.toString(bestProfit)), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            Files.write(file, Arrays.asList("AVG: " + Double.toString(average/100)), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            for(Node node : bestPath) {
                String line = preparePointToWrite(node);
                List<String> lines = Arrays.asList(line);
                Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeExtensions(HashMap<String, List<Node>> extensions, HashMap<String, Double> results) {
        try {
            Path file = Paths.get(fileNameToWrite);
            for (String method : extensions.keySet()) {
                List<Node> salesMan = extensions.get(method);
                System.out.println(results.get(method));
                Files.write(file, Arrays.asList("EXTEND: " + method + " " + results.get(method)), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                for(Node node : salesMan) {
                    String line = preparePointToWrite(node);
                    List<String> lines = Arrays.asList(line);
                    Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String preparePointToWrite(Node node){
        StringBuilder line = new StringBuilder();
        line.append(node.getIndex());
        line.append(' ');
        line.append((int)node.getX());
        line.append(' ');
        line.append((int)node.getY());
        return line.toString();
    }
}
