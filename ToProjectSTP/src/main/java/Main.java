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
    private static final int tabuSize = 5;
    private static final int loopSize = 100;

    public static void main(String[] args) {
        clearResultFile();
        List<Node> nodes = readNodes();
        List<SalesMan> salesMen = prepareSalesMan(nodes);
        findPaths(salesMen);

    }

    private static void clearResultFile() {
        try {
            File file = new File(SalesMan.fileNameToWrite);
            if (file.exists()) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            } else
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Node> readNodes() {
        FileReaderTxt fileReaderTxt = new FileReaderTxt();
        return fileReaderTxt.readFile(fileNameA, fileNameB);
    }

    private static List<SalesMan> prepareSalesMan(List<Node> nodes) {
        List<SalesMan> salesMen = new ArrayList<SalesMan>();
        SalesManRandom randomSalesMan = new SalesManRandom(nodes);
        salesMen.add(randomSalesMan);
        return salesMen;
    }

    private static double countCommonNodes(SalesMan salesMan1, SalesMan salesMan2) {
        ArrayList<Node> nodes1 = (ArrayList<Node>)salesMan1.getPath();
        ArrayList<Node> nodes2 = (ArrayList<Node>)salesMan2.getPath();
        double commonNodesCount = 0;
        for (Node node : nodes1) {
            for (Node temp : nodes2) {
                if (node.getIndex() == temp.getIndex()) {
                    commonNodesCount++;
                    break;
                }
            }
        }
        return commonNodesCount / ((nodes1.size() + nodes2.size()) / 2);
    }

    private static  double countCommonEdges(SalesMan salesMan1, SalesMan salesMan2) {
        ArrayList<Node> nodes1 = (ArrayList<Node>)salesMan1.getPath();
        ArrayList<Node> nodes2 = (ArrayList<Node>)salesMan2.getPath();
        double commonEdgesCount = 0;
        for (Node node : nodes1) {
            for (Node temp : nodes2) {
                if (node.getIndex() != temp.getIndex()) {
                    continue;
                }
                if (node.getNext().getIndex() == temp.getNext().getIndex()
                        || node.getNext().getIndex() == temp.getPrev().getIndex()
                       ) {
                    commonEdgesCount++;
                }
            }
        }
        return commonEdgesCount / ((nodes1.size() - 2 + nodes2.size()) / 2);
    }

    private static void findPaths(List<SalesMan> salesMen) {
        ArrayList<SalesMan> results = new ArrayList<>();
        for (int i = 0; i < loopSize; ++i) {
            SalesManRandom salesMan = new SalesManRandom(readNodes());
            salesMan.findPath(salesMan.getNodes().get(0));
            SalesManNeighbourhood salesManNeighbourhood = new SalesManNeighbourhood(salesMan);
            salesManNeighbourhood.extendCycle();
            results.add(salesMan);
        }

        Double[][] commonNodes = new Double[loopSize][loopSize];
        Double[][] commonEdges = new Double[loopSize][loopSize];
        double avgNodes = 0;
        double avgEdges = 0;
        for (int i = 0; i < loopSize; ++i) {
            for (int j = 0; j < loopSize; ++j) {
                if (i == j) {
                    continue;
                }
                double tempCommonNodes = countCommonNodes(results.get(i), results.get(j));
                double tempCommonEdges = countCommonEdges(results.get(i), results.get(j));
                commonNodes[i][j] = tempCommonNodes;
                commonEdges[i][j] = tempCommonEdges;
                avgNodes += tempCommonNodes;
                avgEdges += tempCommonEdges;
                //System.out.println(results.get(i).getProfit() + "," + tempCommonNodes + "," + tempCommonEdges);
            }
            avgNodes /= loopSize-1;
            avgEdges /= loopSize-1;
            System.out.println(avgNodes + " " + avgEdges);
            avgNodes = 0;
            avgEdges = 0;

        }
    }

}
