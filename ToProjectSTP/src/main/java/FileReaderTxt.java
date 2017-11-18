import SalesMan.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReaderTxt {

    public List<Node> readFile(String fileNameA, String fileNameB){
        List<Node> nodes = new ArrayList<Node>();
        try {

            BufferedReader brA = getBufferedReader(fileNameA);
            BufferedReader brB = getBufferedReader(fileNameB);
            String lineA;
            String lineB;
            while ((lineA = brA.readLine())!= null  && (lineB = brB.readLine()) != null){
                if(lineA.matches("\\d+\\s\\d+\\s\\d+")) {
                    String[] splitValuesA = lineA.split("[ ]");
                    String[] splitValuesB = lineB.split("[ ]");
                    nodes.add(new Node(Integer.parseInt(splitValuesA[0]), Double.parseDouble(splitValuesA[1]),
                            Double.parseDouble(splitValuesA[2]), Double.parseDouble(splitValuesB[1])));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    private BufferedReader getBufferedReader(String filename){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());
            FileReader fr = new FileReader(file);
            return new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void prepareNode(String lineA, String lineB, List<Node> nodes){
        if(lineA.matches("\\d+\\s\\d+\\s\\d+")) {
            String[] splitValuesA = lineA.split("[ ]");
            String[] splitValuesB = lineB.split("[ ]");
            nodes.add(new Node(Integer.parseInt(splitValuesA[0]), Double.parseDouble(splitValuesA[1]),
                    Double.parseDouble(splitValuesA[2]), Double.parseDouble(splitValuesB[1])));
        }
    }
}
