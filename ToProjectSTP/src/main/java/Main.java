import SalesMan.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {

    private static final String fileNameA = "kroA100.txt";
    private static final String fileNameB = "kroB100.txt";
    private static final int tabuSize = 0;

    public static void main(String [] args) {
        clearResultFile();
        List<Node> nodes = readNodes();
        List<SalesMan> salesMen = prepareSalesMan(nodes);
        findPaths(salesMen);

    }

    private static void clearResultFile(){
        try {
            File file = new File(SalesMan.fileNameToWrite);
            if(file.exists()) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
            else
                file.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Node> readNodes(){
        FileReaderTxt fileReaderTxt = new FileReaderTxt();
        return fileReaderTxt.readFile(fileNameA, fileNameB);
    }

    private static List<SalesMan> prepareSalesMan(List<Node> nodes){
        List<SalesMan> salesMen = new ArrayList<SalesMan>();
        SalesManNearestNeighbor nn = new SalesManNearestNeighbor(nodes);
        salesMen.add(nn);
        SalesManGreedyCycle gc = new SalesManGreedyCycle(nodes);
        salesMen.add(gc);
        SalesManGreedyCycleWithRegret gcr = new SalesManGreedyCycleWithRegret(nodes);
        salesMen.add(gcr);
        SalesManRandom randomSalesMan = new SalesManRandom(nodes);
        salesMen.add(randomSalesMan);
        return salesMen;
    }

    private static void findPaths(List<SalesMan> salesMen){
        HashMap<String, List<Node>> bestSalesMan = new HashMap<String, List<Node>>();
        HashMap<String, Double> bestResults = new HashMap<String, Double>();
        HashMap<String, Long> times = new HashMap<String, Long>();
        long sum = 0;
        for(SalesMan salesMan : salesMen) {
            for (Node node : salesMan.getNodes()) {
                    salesMan.getTabu().setTabuList(new ArrayList<ExtendingNode>());
                    salesMan.getTabu().setSize(tabuSize);
                    salesMan.findPath(node);
                    SalesManNeighbourhood salesManNeighbourhood = new SalesManNeighbourhood(salesMan);
                    long startTime = System.currentTimeMillis();
                    SalesMan salesManExtend = salesManNeighbourhood.extendCycle();
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    sum += elapsedTime;
//                System.out.println(elapsedTime);
                    if (times.get(salesManExtend.getClass().toString()) == null) {
                        times.put(salesManExtend.getClass().toString(), elapsedTime);
                    } else {
                        times.put(salesManExtend.getClass().toString(), times.get(salesManExtend.getClass().toString()) + elapsedTime);
                    }
                    if (bestResults.get(salesManExtend.getClass().toString()) == null) {
                        bestSalesMan.put(salesManExtend.getClass().toString(), salesManExtend.getVisitedNodes());
                        bestResults.put(salesManExtend.getClass().toString(), salesManExtend.getActualProfit());
                    }
                    double bestResult = bestResults.get(salesManExtend.getClass().toString());
                    if (salesManExtend.getActualProfit() > bestResult) {
                        bestSalesMan.put(salesManExtend.getClass().toString(), salesManExtend.getVisitedNodes());
                        bestResults.put(salesManExtend.getClass().toString(), salesManExtend.getActualProfit());
                    }

            }
            salesMan.writeBestResult();
        }
        salesMen.get(0).writeExtensions(bestSalesMan, bestResults);
        System.out.println(times);
        System.out.printf("sum" + sum);
    }

}
