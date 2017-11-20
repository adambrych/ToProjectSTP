import SalesMan.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String fileNameA = "kroA100.txt";
    private static final String fileNameB = "kroB100.txt";

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
        //SalesManGreedyCycleWithRegret gcr = new SalesManGreedyCycleWithRegret(nodes);
        //salesMen.add(gcr);
        return salesMen;
    }

    private static void findPaths(List<SalesMan> salesMen){
        for(SalesMan salesMan : salesMen) {
            for (Node node : salesMan.getNodes()) {
                salesMan.findPath(node);
            }
            salesMan.writeBestResult();
        }
    }

}
